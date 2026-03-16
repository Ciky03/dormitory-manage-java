package cloud.ciky.business.service;

import cloud.ciky.base.BaseQuery;
import cloud.ciky.business.model.entity.UserTeacher;
import cloud.ciky.business.model.form.UserTeacherForm;
import cloud.ciky.business.model.vo.TeacherPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
     * 获取教师分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.TeacherPageVO>
     */
    Page<TeacherPageVO> listTeacher(BaseQuery query);

    /**
     * <p>
     * 获取教师表单
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param id 主键
     * @return cloud.ciky.business.model.form.UserTeacherForm
     */
    UserTeacherForm getTeacherForm(String id);

    /**
     * <p>
     * 删除教师
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param id 主键
     * @return boolean
     */
    boolean deleteTeacher(String id);

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
