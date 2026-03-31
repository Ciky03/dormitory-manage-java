package cloud.ciky.business.service.impl;

import cloud.ciky.base.enums.DelflagEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.business.enums.ConventionStatusEnum;
import cloud.ciky.business.mapper.DmConventionMapper;
import cloud.ciky.business.model.entity.DmConvention;
import cloud.ciky.business.model.form.DmConventionForm;
import cloud.ciky.business.model.vo.DmConventionVO;
import cloud.ciky.business.model.vo.HistoryConventionVO;
import cloud.ciky.business.service.DmConventionService;
import cloud.ciky.business.service.RoomStudentService;
import cloud.ciky.business.utils.UserInfoUtil;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 宿舍公约版本表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-24 19:32:06
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DmConventionServiceImpl extends ServiceImpl<DmConventionMapper, DmConvention> implements DmConventionService {

    private final RoomStudentService roomStudentService;

    @Override
    public DmConventionVO getCurrentConvention() {
        String studentId = UserInfoUtil.getCurrentStudentId();
        return this.baseMapper.selectConventionInfo(studentId, null);
    }

    @Override
    public List<HistoryConventionVO> getHistoryConvention(String roomId) {
        String optUser = SecurityUtils.getUserId();
        List<HistoryConventionVO> vos = this.baseMapper.selectHistoryConvention(roomId);
        if (CollUtil.isNotEmpty(vos)) {
            vos.forEach(vo -> vo.setEditable(Objects.equals(vo.getUpdateUserId(), optUser)));
        }
        return vos;
    }

    @Override
    public DmConventionVO getConventionForm(String id) {
        String studentId = UserInfoUtil.getCurrentStudentId();
        return this.baseMapper.selectConventionInfo(studentId, id);
    }

    @Override
    public boolean saveConvention(DmConventionForm form) {
        String roomId = form.getRoomId();
        String id = form.getId();
        String optUser = SecurityUtils.getUserId();
        DmConvention entity = new DmConvention();
        if (CharSequenceUtil.isNotBlank(id)) {
            DmConvention convention = getConvention(id, roomId);
            if (convention.getStatus() == null || !convention.getStatus().equals(ConventionStatusEnum.DRAFT.getValue())) {
                throw new BusinessException("仅草稿状态可编辑");
            }
            entity.setId(id);
            entity.setUpdateBy(optUser);
        } else {
            entity.setCreateBy(optUser);
        }
        entity.setRoomId(roomId);
        entity.setVersionNo(null);
        entity.setIsCurrent(false);
        entity.setTitle(form.getTitle());
        entity.setContentMd(form.getContentMd());
        entity.setStatus(ConventionStatusEnum.DRAFT.getValue());
        return this.saveOrUpdate(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishConvention(String id) {
        String roomId =roomStudentService.getSelectedRoomIdThrowExp(UserInfoUtil.getCurrentStudentId());
        DmConvention entity = getConvention(id, roomId);
        if (entity.getStatus() == null || entity.getStatus() != ConventionStatusEnum.DRAFT.getValue()) {
            throw new BusinessException("仅草稿状态可发布");
        }

        String userId = SecurityUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();
        DmConvention latest = this.getOne(new LambdaQueryWrapper<DmConvention>()
                .eq(DmConvention::getRoomId, roomId)
                .eq(DmConvention::getIsCurrent, true)
                .eq(DmConvention::getDelflag, DelflagEnum.USABLE.getValue()));
        Integer newVersionNo = latest == null ? 1 : latest.getVersionNo() + 1;

        this.update(new LambdaUpdateWrapper<DmConvention>()
                .set(DmConvention::getIsCurrent, false)
                .set(DmConvention::getUpdateBy, userId)
                .set(DmConvention::getUpdateTime, now)
                .eq(DmConvention::getRoomId, roomId)
                .eq(DmConvention::getIsCurrent, true)
                .eq(DmConvention::getDelflag, DelflagEnum.USABLE.getValue()));

        return this.update(new LambdaUpdateWrapper<DmConvention>()
                .set(DmConvention::getStatus, ConventionStatusEnum.PUBLISHED.getValue())
                .set(DmConvention::getVersionNo, newVersionNo)
                .set(DmConvention::getIsCurrent, true)
                .set(DmConvention::getPublishBy, userId)
                .set(DmConvention::getPublishTime, now)
                .set(DmConvention::getUpdateBy, userId)
                .set(DmConvention::getUpdateTime, now)
                .eq(DmConvention::getId, id)
                .eq(DmConvention::getRoomId, roomId)
                .eq(DmConvention::getStatus, ConventionStatusEnum.DRAFT.getValue())
                .eq(DmConvention::getDelflag, DelflagEnum.USABLE.getValue()));
    }

    @Override
    public DmConvention getCurrentRoomConvention(String id) {
        return getConvention(id, roomStudentService.getSelectedRoomIdThrowExp(UserInfoUtil.getCurrentStudentId()));
    }

    /**
     * 获取宿舍指定公约
     */
    private DmConvention getConvention(String id, String roomId) {
        DmConvention entity = this.getOne(new LambdaQueryWrapper<DmConvention>()
                .eq(DmConvention::getId, id)
                .eq(DmConvention::getRoomId, roomId)
                .eq(DmConvention::getDelflag, DelflagEnum.USABLE.getValue())
                .last("limit 1"));
        if (entity == null) {
            throw new BusinessException("公约不存在或无访问权限");
        }
        return entity;
    }

}

