package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.UserTeacher;
import cloud.ciky.business.model.form.UserTeacherForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 教师表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-03-14 11:40:01
 */
public interface UserTeacherService extends IService<UserTeacher> {

    /**
     * <p>
     * 保存教师
     * </p>
     *
     * @author ciky
     * @since 2026/3/14 11:49
     * @param form 表单对象
     * @return boolean
     */
    boolean saveTeacher(UserTeacherForm form);
}
