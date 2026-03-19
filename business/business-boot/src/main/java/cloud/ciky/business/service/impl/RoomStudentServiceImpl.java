package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.RoomStudent;
import cloud.ciky.business.model.form.RoomStudentForm;
import cloud.ciky.business.mapper.RoomStudentMapper;
import cloud.ciky.business.service.RoomStudentService;
import cloud.ciky.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 学生宿舍关联表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-19 16:43:50
 */
@Slf4j
@Service
public class RoomStudentServiceImpl extends ServiceImpl<RoomStudentMapper, RoomStudent> implements RoomStudentService {

    @Override
    public String getSelectedRoomId(String studentId) {
        RoomStudent roomStudent = this.getOne(new LambdaQueryWrapper<RoomStudent>()
                .eq(RoomStudent::getStudentId, studentId)
                .eq(RoomStudent::getIsCurrent, true));
        return roomStudent != null ? roomStudent.getRoomId() : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRoomStudent(RoomStudentForm form) {
        String studentId = form.getStudentId();
        String roomId = form.getRoomId();
        String userId = SecurityUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();

        this.update(new LambdaUpdateWrapper<RoomStudent>()
                .set(RoomStudent::getIsCurrent, false)
                .set(RoomStudent::getEndTime, now)
                .set(RoomStudent::getUpdateBy, userId)
                .set(RoomStudent::getUpdateTime, now)
                .eq(RoomStudent::getStudentId, studentId)
                .eq(RoomStudent::getIsCurrent, true));

        RoomStudent entity = new RoomStudent();
        entity.setStudentId(studentId);
        entity.setRoomId(roomId);
        entity.setIsCurrent(true);
        entity.setStartTime(now);
        entity.setCreateBy(userId);
        entity.setCreateTime(now);
        return this.save(entity);
    }
}
