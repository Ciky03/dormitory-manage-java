package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.ClassTeacher;
import cloud.ciky.business.mapper.ClassTeacherMapper;
import cloud.ciky.business.service.ClassTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
