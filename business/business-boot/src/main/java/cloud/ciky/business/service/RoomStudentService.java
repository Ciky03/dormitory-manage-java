package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.RoomStudent;
import cloud.ciky.business.model.form.RoomStudentForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 学生宿舍关联表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-03-19 16:43:50
 */
public interface RoomStudentService extends IService<RoomStudent> {

    /**
     * <p>
     * 获取当前学生所在宿舍
     * </p>
     *
     * @author ciky
     * @since 2026/3/19 17:04
     * @param studentId 学生id
     * @return java.lang.String
     */
    String getSelectedRoomId(String studentId);

    /**
     * <p>
     * 保存学生宿舍信息
     * </p>
     *
     * @author ciky
     * @since 2026/3/19 17:33
     * @param form 表单对象
     * @return boolean
     */
    boolean saveRoomStudent(RoomStudentForm form);

}
