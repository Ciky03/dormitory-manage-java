package cloud.ciky.business.service.impl;

import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.business.mapper.DmConventionAckMapper;
import cloud.ciky.business.model.entity.DmConventionAck;
import cloud.ciky.business.model.form.DmConventionAckForm;
import cloud.ciky.business.model.vo.DmConventionAckStateVO;
import cloud.ciky.business.model.vo.DmMemberConventionAckVO;
import cloud.ciky.business.service.DmConventionAckService;
import cloud.ciky.business.service.DmConventionService;
import cloud.ciky.business.service.RoomStudentService;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 宿舍公约阅读/同意记录表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-24 19:32:07
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DmConventionAckServiceImpl extends ServiceImpl<DmConventionAckMapper, DmConventionAck> implements DmConventionAckService {

    private final DmConventionService conventionService;
    private final RoomStudentService roomStudentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean ackConvention(DmConventionAckForm form) {
        String studentId = getCurrentStudentId();
        String optUser = SecurityUtils.getUserId();
        String conventionId = form.getConventionId();
        Boolean agreeFlag = form.getAgreeFlag() != null && form.getAgreeFlag();
        LocalDateTime now = LocalDateTime.now();
        // 查询当前宿舍合约是否存在
        conventionService.getCurrentRoomConvention(conventionId);

        // 查询当前学生阅读情况
        DmConventionAck ack = this.getOne(new LambdaQueryWrapper<DmConventionAck>()
                .eq(DmConventionAck::getConventionId, conventionId)
                .eq(DmConventionAck::getStudentId, studentId)
                .last("limit 1"));

        DmConventionAck entity = new DmConventionAck();
        if (ObjUtil.isNotNull(ack)) {
            entity.setId(ack.getId());
            entity.setUpdateBy(optUser);
        } else {
            entity.setCreateBy(optUser);
        }
        entity.setConventionId(conventionId);
        entity.setStudentId(studentId);
        entity.setReadTime(ObjUtil.isNull(ack) ? now : (ObjUtil.isNull(ack.getReadTime()) ? now : ack.getReadTime()));
        entity.setAgreeFlag(agreeFlag);
        entity.setAgreeTime(agreeFlag ? now : null);
        return this.saveOrUpdate(entity);
    }

    @Override
    public DmConventionAckStateVO getAckStat(String conventionId) {
        // 查询当前宿舍合约是否存在
        conventionService.getCurrentRoomConvention(conventionId);
        String studentId = getCurrentStudentId();

        // 获取宿舍成员合约同意情况列表
        List<DmMemberConventionAckVO> memberAckList = this.baseMapper.listMemberConventionAck(conventionId);

        DmConventionAckStateVO stateVO = new DmConventionAckStateVO();
        AtomicReference<Integer> agreedCount = new AtomicReference<>(0);
        memberAckList.forEach(ack -> {
            if (Boolean.TRUE.equals(ack.getAgreeFlag())) {
                agreedCount.getAndSet(agreedCount.get() + 1);
            }
            ack.setAgreeStatus(Boolean.TRUE.equals(ack.getAgreeFlag()) ? 2 : (ack.getReadTime() != null ? 1 : 0));
            if (ack.getStudentId().equals(studentId)) {
                stateVO.setAgreeStatus(ack.getAgreeStatus());
            }
        });
        stateVO.setAgreedCount(agreedCount.get());
        stateVO.setTotalCount(memberAckList.size());
        stateVO.setMemberAckList(memberAckList);
        return stateVO;
    }

    private String getCurrentStudentId() {
        String studentId = SecurityUtils.getBusinessUserId();
        if (CharSequenceUtil.isBlank(studentId)) {
            throw new BusinessException("未识别到当前学生信息");
        }
        return studentId;
    }
}

