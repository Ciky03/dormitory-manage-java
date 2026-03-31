package cloud.ciky.business.service.impl;

import cloud.ciky.base.IBaseEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.business.enums.DmSharedCostPayStatusEnum;
import cloud.ciky.business.enums.DmSharedCostStatusEnum;
import cloud.ciky.business.mapper.DmSharedCostMapper;
import cloud.ciky.business.model.entity.DmSharedCost;
import cloud.ciky.business.model.entity.DmSharedCostDetail;
import cloud.ciky.business.model.form.DmSharedCostForm;
import cloud.ciky.business.model.form.DmSharedCostMemberForm;
import cloud.ciky.business.model.form.DmSharedCostPayForm;
import cloud.ciky.business.model.query.DmSharedCostPageQuery;
import cloud.ciky.business.model.vo.DmSharedCostDetailVO;
import cloud.ciky.business.model.vo.DmSharedCostMemberVO;
import cloud.ciky.business.model.vo.DmSharedCostPageVO;
import cloud.ciky.business.model.vo.DmSharedCostStatVO;
import cloud.ciky.business.service.DmSharedCostDetailService;
import cloud.ciky.business.service.DmSharedCostService;
import cloud.ciky.business.service.RoomStudentService;
import cloud.ciky.business.utils.UserInfoUtil;
import cloud.ciky.file.model.dto.TempUrlDTO;
import cloud.ciky.file.service.OssService;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * <p>
 * 宿舍费用公摊主表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-31 12:04:45
 */
@Service
@RequiredArgsConstructor
public class DmSharedCostServiceImpl extends ServiceImpl<DmSharedCostMapper, DmSharedCost> implements DmSharedCostService {

    private final RoomStudentService roomStudentService;
    private final DmSharedCostDetailService dmSharedCostDetailService;
    private final OssService ossService;

    @Override
    public DmSharedCostStatVO getSharedCostStat() {
        String studentId = UserInfoUtil.getCurrentStudentId();
        DmSharedCostStatVO statVO = this.baseMapper.selectSharedCostStat(studentId);
        if (ObjUtil.isNotNull(statVO)) {
            statVO.setMyUnpaidAmount(defaultAmount(statVO.getMyUnpaidAmount()));
            statVO.setRoomUnpaidAmount(defaultAmount(statVO.getRoomUnpaidAmount()));
            statVO.setCurrentMonthRoomUnpaidAmount(defaultAmount(statVO.getCurrentMonthRoomUnpaidAmount()));
        }
        return statVO;
    }

    @Override
    public Page<DmSharedCostPageVO> listSharedCost(DmSharedCostPageQuery query) {
        String studentId = UserInfoUtil.getCurrentStudentId();
        query.setStudentId(studentId);
        query.setRoomId(roomStudentService.getSelectedRoomIdThrowExp(studentId));
        Page<DmSharedCostPageVO> page = this.baseMapper.selectSharedCostPage(new Page<>(query.getPageNum(), query.getPageSize()), query);

        // 填充标签
        if (CollUtil.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(this::fillPageLabels);
        }
        return page;
    }

    @Override
    public DmSharedCostDetailVO getSharedCostDetail(String id) {
        String studentId = UserInfoUtil.getCurrentStudentId();
        String roomId = roomStudentService.getSelectedRoomIdThrowExp(studentId);

        // 查询费用公摊单
        DmSharedCostDetailVO detailVO = this.baseMapper.selectSharedCostDetail(id, roomId);
        if (detailVO == null) {
            throw new BusinessException("公摊单不存在或无权访问");
        }
        detailVO.setStatusLabel(IBaseEnum.getLabelByValue(detailVO.getStatus(), DmSharedCostStatusEnum.class));
        detailVO.setSourceVoucherUrl(buildAttachUrl(detailVO.getSourceVoucherBucket(), detailVO.getSourceVoucherPath()));

        // 查询费用公摊明细
        List<DmSharedCostMemberVO> memberList = dmSharedCostDetailService.listSharedCostMember(id, studentId);
        detailVO.setMemberList(memberList);

        // 设置按钮权限
        boolean currentUserUnpaid = memberList.stream().anyMatch(member -> Boolean.TRUE.equals(member.getIsCurrentUser())
                && Objects.equals(member.getPayStatus(), DmSharedCostPayStatusEnum.UNPAID.getValue()));
        boolean isInitiator = Objects.equals(detailVO.getInitiatorStudentId(), studentId);
        detailVO.setCanEdit(isInitiator && Objects.equals(detailVO.getStatus(), DmSharedCostStatusEnum.DRAFT.getValue()));
        detailVO.setCanPublish(isInitiator && Objects.equals(detailVO.getStatus(), DmSharedCostStatusEnum.DRAFT.getValue()));
        detailVO.setCanCancel(isInitiator && Objects.equals(detailVO.getStatus(), DmSharedCostStatusEnum.PUBLISHED.getValue()));
        detailVO.setCanPay(currentUserUnpaid && Objects.equals(detailVO.getStatus(), DmSharedCostStatusEnum.PUBLISHED.getValue()));
        return detailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveSharedCost(DmSharedCostForm form) {
        String studentId = UserInfoUtil.getCurrentStudentId();
        String roomId = roomStudentService.getSelectedRoomIdThrowExp(studentId);
        String optUser = SecurityUtils.getUserId();

        // 解析&校验费用公摊明细
        BigDecimal totalAmount = form.getTotalAmount();
        List<DmSharedCostMemberForm> memberForms = resolveMemberForms(form, roomId);
        validateMemberForms(roomId, totalAmount, memberForms);

        String id = form.getId();
        DmSharedCost entity = new DmSharedCost();
        if (CharSequenceUtil.isNotBlank(id)) {
            // 编辑
            DmSharedCost current = requireCurrentRoomCost(id, roomId);
            if (!Objects.equals(current.getInitiatorStudentId(), studentId)) {
                throw new BusinessException("仅发起人可编辑该公摊单");
            }
            if (!Objects.equals(current.getStatus(), DmSharedCostStatusEnum.DRAFT.getValue())) {
                throw new BusinessException("仅草稿状态可编辑");
            }
            entity.setId(id);
            entity.setInitiatorStudentId(current.getInitiatorStudentId());
            entity.setUpdateBy(optUser);
        } else {
            // 新增
            entity.setInitiatorStudentId(studentId);
            entity.setCreateBy(optUser);
        }
        entity.setRoomId(roomId);
        entity.setTitle(form.getTitle());
        entity.setTotalAmount(totalAmount);
        entity.setOccurredDate(form.getOccurredDate());
        entity.setDueTime(form.getDueTime());
        entity.setStatus(DmSharedCostStatusEnum.DRAFT.getValue());
        entity.setRemark(form.getRemark());
        entity.setSourceVoucherAttachId(form.getSourceVoucherAttachId());
        boolean saved = this.saveOrUpdate(entity);
        return saved && dmSharedCostDetailService.replaceCostDetails(entity.getId(), memberForms, optUser);
    }

    @Override
    public boolean publishSharedCost(String id) {
        String studentId = UserInfoUtil.getCurrentStudentId();
        String roomId = roomStudentService.getSelectedRoomIdThrowExp(studentId);
        DmSharedCost current = requireCurrentRoomCost(id, roomId);
        if (!Objects.equals(current.getInitiatorStudentId(), studentId)) {
            throw new BusinessException("仅发起人可发布该公摊单");
        }
        if (!Objects.equals(current.getStatus(), DmSharedCostStatusEnum.DRAFT.getValue())) {
            throw new BusinessException("仅草稿状态可发布");
        }
        current.setStatus(DmSharedCostStatusEnum.PUBLISHED.getValue());
        current.setUpdateBy(SecurityUtils.getUserId());
        return this.updateById(current);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean paySharedCost(String detailId, DmSharedCostPayForm form) {
        String studentId = UserInfoUtil.getCurrentStudentId();
        String roomId = roomStudentService.getSelectedRoomIdThrowExp(studentId);

        // 获取当前学生可支付的公摊明细
        DmSharedCostDetail detail = dmSharedCostDetailService.requirePayableDetail(detailId, studentId);
        DmSharedCost current = requireCurrentRoomCost(detail.getCostId(), roomId);
        if (!Objects.equals(current.getStatus(), DmSharedCostStatusEnum.PUBLISHED.getValue())) {
            throw new BusinessException("仅已发布状态可缴费");
        }
        boolean updated = dmSharedCostDetailService.payDetail(detailId, studentId, form.getVoucherAttachId(), SecurityUtils.getUserId());

        // 统计公摊单未缴明细数量
        Integer unpaidCount = dmSharedCostDetailService.countUnpaidDetail(current.getId());
        if (updated && unpaidCount != null && unpaidCount == 0) {
            current.setStatus(DmSharedCostStatusEnum.COMPLETED.getValue());
            current.setUpdateBy(SecurityUtils.getUserId());
            return this.updateById(current);
        }
        return updated;
    }

    @Override
    public boolean cancelSharedCost(String id) {
        String studentId = UserInfoUtil.getCurrentStudentId();
        String roomId = roomStudentService.getSelectedRoomIdThrowExp(studentId);
        DmSharedCost current = requireCurrentRoomCost(id, roomId);

        if (!Objects.equals(current.getInitiatorStudentId(), studentId)) {
            throw new BusinessException("仅发起人可取消该公摊单");
        }
        if (!Objects.equals(current.getStatus(), DmSharedCostStatusEnum.PUBLISHED.getValue())) {
            throw new BusinessException("仅已发布状态可取消");
        }
        current.setStatus(DmSharedCostStatusEnum.CANCELED.getValue());
        current.setUpdateBy(SecurityUtils.getUserId());
        return this.updateById(current);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSharedCost(String id) {
        String studentId = UserInfoUtil.getCurrentStudentId();
        String roomId = roomStudentService.getSelectedRoomIdThrowExp(studentId);
        DmSharedCost current = requireCurrentRoomCost(id, roomId);
        if (!Objects.equals(current.getInitiatorStudentId(), studentId)) {
            throw new BusinessException("仅发起人可删除该公摊单");
        }
        if (!Objects.equals(current.getStatus(), DmSharedCostStatusEnum.DRAFT.getValue())) {
            throw new BusinessException("仅草稿状态可删除");
        }
        current.setDelflag(true);
        current.setUpdateBy(SecurityUtils.getUserId());
        return this.updateById(current) && dmSharedCostDetailService.deleteCostDetails(current.getId(), SecurityUtils.getUserId());
    }

    private DmSharedCost requireCurrentRoomCost(String id, String roomId) {
        DmSharedCost entity = this.getById(id);
        if (entity == null || Boolean.TRUE.equals(entity.getDelflag()) || !Objects.equals(entity.getRoomId(), roomId)) {
            throw new BusinessException("公摊单不存在或无权访问");
        }
        return entity;
    }

    private List<DmSharedCostMemberForm> resolveMemberForms(DmSharedCostForm form, String roomId) {
        if (CollUtil.isNotEmpty(form.getMemberList())) {
            return form.getMemberList();
        }

        // 前端没传, 则默认当前宿舍所有成员均摊 (最后一个人承担差值)
        List<DmSharedCostMemberVO> currentRoomMembers = this.baseMapper.selectCurrentRoomMemberList(roomId);
        if (CollUtil.isEmpty(currentRoomMembers)) {
            throw new BusinessException("当前宿舍暂无成员，无法创建公摊单");
        }
        BigDecimal totalAmount = form.getTotalAmount().setScale(2, RoundingMode.HALF_UP);
        BigDecimal average = totalAmount.divide(BigDecimal.valueOf(currentRoomMembers.size()), 2, RoundingMode.DOWN);
        BigDecimal assigned = average.multiply(BigDecimal.valueOf(Math.max(currentRoomMembers.size() - 1L, 0L)));
        List<DmSharedCostMemberForm> memberForms = new ArrayList<>();
        for (int i = 0; i < currentRoomMembers.size(); i++) {
            DmSharedCostMemberVO memberVO = currentRoomMembers.get(i);
            DmSharedCostMemberForm memberForm = new DmSharedCostMemberForm();
            memberForm.setStudentId(memberVO.getStudentId());
            memberForm.setAmountDue(i == currentRoomMembers.size() - 1 ? totalAmount.subtract(assigned) : average);
            memberForms.add(memberForm);
        }
        return memberForms;
    }

    private void validateMemberForms(String roomId, BigDecimal totalAmount, List<DmSharedCostMemberForm> memberForms) {
        if (CollUtil.isEmpty(memberForms)) {
            throw new BusinessException("公摊成员不能为空");
        }
        Set<String> studentIds = new HashSet<>();
        BigDecimal sum = BigDecimal.ZERO;
        for (DmSharedCostMemberForm memberForm : memberForms) {
            if (CharSequenceUtil.isBlank(memberForm.getStudentId())) {
                throw new BusinessException("公摊成员不能为空");
            }
            if (memberForm.getAmountDue() == null) {
                throw new BusinessException("成员应缴金额不能为空");
            }
            if (!studentIds.add(memberForm.getStudentId())) {
                throw new BusinessException("公摊成员不能重复");
            }
            Integer count = this.baseMapper.countCurrentRoomStudent(roomId, memberForm.getStudentId());
            if (count == null || count == 0) {
                throw new BusinessException("公摊成员必须为当前宿舍成员");
            }
            sum = sum.add(memberForm.getAmountDue());
        }
        if (sum.compareTo(totalAmount) != 0) {
            throw new BusinessException("明细金额总和必须等于公摊总金额");
        }
    }

    private void fillPageLabels(DmSharedCostPageVO pageVO) {
        pageVO.setStatusLabel(IBaseEnum.getLabelByValue(pageVO.getStatus(), DmSharedCostStatusEnum.class));
        pageVO.setMyPayStatusLabel(pageVO.getMyPayStatus() == null ? null : (pageVO.getMyPayStatus() == 1 ? "已缴" : "未缴"));
    }

    private String buildAttachUrl(String bucket, String path) {
        if (CharSequenceUtil.isBlank(bucket) || CharSequenceUtil.isBlank(path)) {
            return null;
        }
        return ossService.getTempUrl(new TempUrlDTO(bucket, path));
    }

    private BigDecimal defaultAmount(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
