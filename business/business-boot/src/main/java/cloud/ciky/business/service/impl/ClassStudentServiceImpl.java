package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.ClassStudent;
import cloud.ciky.business.mapper.ClassStudentMapper;
import cloud.ciky.business.service.ClassStudentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
