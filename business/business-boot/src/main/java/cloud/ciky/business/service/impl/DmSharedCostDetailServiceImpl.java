package cloud.ciky.business.service.impl;

import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.business.enums.DmSharedCostPayStatusEnum;
import cloud.ciky.business.mapper.DmSharedCostDetailMapper;
import cloud.ciky.business.model.entity.DmSharedCostDetail;
import cloud.ciky.business.model.form.DmSharedCostMemberForm;
import cloud.ciky.business.model.vo.DmSharedCostMemberVO;
import cloud.ciky.business.service.DmSharedCostDetailService;
import cloud.ciky.file.model.dto.TempUrlDTO;
import cloud.ciky.file.service.OssService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 宿舍费用公摊明细表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-31 12:04:45
 */
@Service
@RequiredArgsConstructor
public class DmSharedCostDetailServiceImpl extends ServiceImpl<DmSharedCostDetailMapper, DmSharedCostDetail> implements DmSharedCostDetailService {

    private final OssService ossService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean replaceCostDetails(String costId, List<DmSharedCostMemberForm> memberList, String userId) {
        if (CollUtil.isEmpty(memberList)) {
            throw new BusinessException("公摊成员不能为空");
        }
        this.deleteCostDetails(costId, userId);
        return this.saveBatch(buildDetails(costId, memberList, userId));
    }

    @Override
    public boolean deleteCostDetails(String costId, String userId) {
        this.update(new LambdaUpdateWrapper<DmSharedCostDetail>()
                .set(DmSharedCostDetail::getDelflag, true)
                .set(DmSharedCostDetail::getUpdateBy, userId)
                .set(DmSharedCostDetail::getUpdateTime, LocalDateTime.now())
                .eq(DmSharedCostDetail::getCostId, costId)
                .eq(DmSharedCostDetail::getDelflag, false));
        return true;
    }

    @Override
    public DmSharedCostDetail requirePayableDetail(String detailId, String studentId) {
        DmSharedCostDetail detail = this.getById(detailId);
        if (detail == null || Boolean.TRUE.equals(detail.getDelflag())) {
            throw new BusinessException("公摊明细不存在或无权访问");
        }
        if (!Objects.equals(detail.getStudentId(), studentId)) {
            throw new BusinessException("仅可支付自己的缴费明细");
        }
        if (Objects.equals(detail.getPayStatus(), DmSharedCostPayStatusEnum.PAID.getValue())) {
            throw new BusinessException("该明细已完成缴费");
        }
        return detail;
    }

    @Override
    public boolean payDetail(String detailId, String studentId, String voucherAttachId, String userId) {
        if (CharSequenceUtil.isBlank(voucherAttachId)) {
            throw new BusinessException("请上传缴费凭证");
        }
        DmSharedCostDetail detail = requirePayableDetail(detailId, studentId);
        detail.setPayStatus(DmSharedCostPayStatusEnum.PAID.getValue());
        detail.setPaidTime(LocalDateTime.now());
        detail.setVoucherAttachId(voucherAttachId);
        detail.setUpdateBy(userId);
        return this.updateById(detail);
    }

    @Override
    public Integer countUnpaidDetail(String costId) {
        Long count = this.count(new LambdaQueryWrapper<DmSharedCostDetail>()
                .eq(DmSharedCostDetail::getCostId, costId)
                .eq(DmSharedCostDetail::getDelflag, false)
                .eq(DmSharedCostDetail::getPayStatus, DmSharedCostPayStatusEnum.UNPAID.getValue()));
        return count == null ? 0 : count.intValue();
    }

    private List<DmSharedCostDetail> buildDetails(String costId, List<DmSharedCostMemberForm> memberList, String userId) {
        List<DmSharedCostDetail> details = new ArrayList<>();
        for (DmSharedCostMemberForm memberForm : memberList) {
            DmSharedCostDetail detail = new DmSharedCostDetail();
            detail.setCostId(costId);
            detail.setStudentId(memberForm.getStudentId());
            detail.setAmountDue(memberForm.getAmountDue());
            detail.setPayStatus(DmSharedCostPayStatusEnum.UNPAID.getValue());
            detail.setCreateBy(userId);
            detail.setDelflag(false);
            details.add(detail);
        }
        return details;
    }

    @Override
    public List<DmSharedCostMemberVO> listSharedCostMember(String costId, String studentId) {
        List<DmSharedCostMemberVO> memberList = this.baseMapper.listSharedCostMember(costId, studentId);
        // TODO 修改成批量更新
        if (CollUtil.isNotEmpty(memberList)) {
            memberList.forEach(this::fillMemberInfo);
        }
        return memberList;
    }

    private void fillMemberInfo(DmSharedCostMemberVO memberVO) {
        memberVO.setPayStatusLabel(memberVO.getPayStatus() == null ? null : (memberVO.getPayStatus() == 1 ? "已缴" : "未缴"));
        memberVO.setVoucherUrl(buildAttachUrl(memberVO.getVoucherBucket(), memberVO.getVoucherPath()));
        if (memberVO.getIsCurrentUser() == null) {
            memberVO.setIsCurrentUser(false);
        }
    }

    private String buildAttachUrl(String bucket, String path) {
        if (CharSequenceUtil.isBlank(bucket) || CharSequenceUtil.isBlank(path)) {
            return null;
        }
        return ossService.getTempUrl(new TempUrlDTO(bucket, path));
    }
}
