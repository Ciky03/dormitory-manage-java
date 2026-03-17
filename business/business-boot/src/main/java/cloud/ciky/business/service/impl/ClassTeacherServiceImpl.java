package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.ClassTeacher;
import cloud.ciky.business.model.form.ClassTeacherForm;
import cloud.ciky.business.mapper.ClassTeacherMapper;
import cloud.ciky.business.service.ClassTeacherService;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 班级教师关联表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-17 11:25:44
 */
@Slf4j
@Service
public class ClassTeacherServiceImpl extends ServiceImpl<ClassTeacherMapper, ClassTeacher> implements ClassTeacherService {

    @Override
    public String getSelectedClassId(String teacherId) {
        ClassTeacher classTeacher = this.getOne(new LambdaQueryWrapper<ClassTeacher>()
                .eq(ClassTeacher::getTeacherId, teacherId)
                .eq(ClassTeacher::getIsCurrent, true));
        return classTeacher != null ? classTeacher.getClassId() : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveClassTeacher(ClassTeacherForm form) {
        String teacherId = form.getTeacherId();
        String classId = form.getClassId();
        String userId = SecurityUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();

        // 更改为历史任职记录
        this.update(new LambdaUpdateWrapper<ClassTeacher>()
                .set(ClassTeacher::getIsCurrent, false)
                .set(ClassTeacher::getEndTime, now)
                .set(ClassTeacher::getUpdateBy, userId)
                .set(ClassTeacher::getUpdateTime, now)
                .eq(ClassTeacher::getClassId, classId) // 保存原班级的班主任信息
                .eq(ClassTeacher::getIsCurrent, true));

        ClassTeacher entity = new ClassTeacher();
        entity.setTeacherId(teacherId);
        entity.setClassId(classId);
        entity.setIsCurrent(true);
        entity.setStartTime(now);
        entity.setCreateBy(userId);
        return this.save(entity);
    }
}
