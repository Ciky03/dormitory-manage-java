package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.UserStudent;
import cloud.ciky.business.model.form.UserStudentForm;
import cloud.ciky.business.model.query.StudentPageQuery;
import cloud.ciky.business.model.vo.RoomMemberVO;
import cloud.ciky.business.model.vo.StudentPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 学生表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-03-12 17:53:23
 */
public interface UserStudentService extends IService<UserStudent> {

    /**
     * <p>
     * 获取学生分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.StudentPageVO>
     */
    Page<StudentPageVO> listStudent(StudentPageQuery query);

    /**
     * <p>
     * 获取学生表单
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param id 主键
     * @return cloud.ciky.business.model.form.UserStudentForm
     */
    UserStudentForm getStudentForm(String id);

    /**
     * <p>
     * 获取当前宿舍成员列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 16:40
     * @return java.util.List<cloud.ciky.business.model.vo.RoomMemberVO>
     */
    List<RoomMemberVO> listCurrentRoomMember();

    /**
     * <p>
     * 删除学生
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param id 主键
     * @return boolean
     */
    boolean deleteStudent(String id);

    /**
     * <p>
     * 保存学生信息
     * </p>
     *
     * @author ciky
     * @since 2026/3/13 11:46
     * @param form 表单对象
     * @return boolean
     */
    boolean saveStudent(UserStudentForm form);
}
