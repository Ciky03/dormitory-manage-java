package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.ClassStudent;
import cloud.ciky.business.model.form.ClassStudentForm;
import cloud.ciky.business.mapper.ClassStudentMapper;
import cloud.ciky.business.service.ClassStudentService;
import cloud.ciky.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 班级学生关联表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-17 11:25:44
 */
@Slf4j
@Service
public class ClassStudentServiceImpl extends ServiceImpl<ClassStudentMapper, ClassStudent> implements ClassStudentService {

    @Override
    public String getSelectedClassId(String studentId) {
        ClassStudent classStudent = this.getOne(new LambdaQueryWrapper<ClassStudent>()
                .eq(ClassStudent::getStudentId, studentId)
                .eq(ClassStudent::getIsCurrent, true));
        return classStudent != null ? classStudent.getClassId() : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveClassStudent(ClassStudentForm form) {
        String studentId = form.getStudentId();
        String classId = form.getClassId();
        String optUser = SecurityUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();

        // 更改为历史班级记录
        this.update(new LambdaUpdateWrapper<ClassStudent>()
                .set(ClassStudent::getIsCurrent, false)
                .set(ClassStudent::getEndTime, now)
                .set(ClassStudent::getUpdateBy, optUser)
                .set(ClassStudent::getUpdateTime, now)
                .eq(ClassStudent::getStudentId, studentId)
                .eq(ClassStudent::getIsCurrent, true));

        ClassStudent entity = new ClassStudent();
        entity.setStudentId(studentId);
        entity.setClassId(classId);
        entity.setIsCurrent(true);
        entity.setStartTime(now);
        entity.setCreateBy(optUser);
        return this.save(entity);
    }
}
