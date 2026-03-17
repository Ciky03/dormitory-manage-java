package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.ClassStudent;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 班级学生关联表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-03-17 11:25:44
 */
public interface ClassStudentService extends IService<ClassStudent> {

    /**
     * <p>
     * 获取当前学生所在班级
     * </p>
     *
     * @author ciky
     * @since 2026/3/17 11:51
     * @param studentId 学生id
     * @return java.lang.String
     */
    String getSelectedClassId(String studentId);

}
