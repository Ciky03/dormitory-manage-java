package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.ClassTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 班级教师关联表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-03-17 11:25:44
 */
public interface ClassTeacherService extends IService<ClassTeacher> {

    /**
     * <p>
     * 获取当前教师任职班级
     * </p>
     *
     * @author ciky
     * @since 2026/3/17 11:53
     * @param teacherId 教师id
     * @return java.lang.String
     */
    String getSelectedClassId(String teacherId);

}
