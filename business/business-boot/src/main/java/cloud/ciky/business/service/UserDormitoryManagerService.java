package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.UserDormitoryManager;
import cloud.ciky.business.model.form.UserDormitoryManagerForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 宿管表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-03-14 11:40:01
 */
public interface UserDormitoryManagerService extends IService<UserDormitoryManager> {

    /**
     * <p>
     * 保存宿管
     * </p>
     *
     * @author ciky
     * @since 2026/3/15 11:33
     * @param form
     * @return boolean
     */
    boolean saveDormitoryManager(UserDormitoryManagerForm form);
}
