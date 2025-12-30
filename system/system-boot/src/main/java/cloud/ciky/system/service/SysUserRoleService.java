package cloud.ciky.system.service;

import cloud.ciky.system.model.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户和角色关联表 服务类
 * </p>
 *
 * @author ciky
 * @since 2025-12-16 15:56:01
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * <p>
     * 查询用户角色code
     * </p>
     *
     * @author ciky
     * @since 2025/12/16 16:41
     * @param userId
     * @return java.util.List<java.lang.String>
     */
    List<String> findRoleCodesByUserId(String userId);
}
