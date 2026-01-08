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

    /**
     * <p>
     * 判断角色是否存在绑定的用户
     * </p>
     *
     * @author ciky
     * @since 2026/1/4 18:15
     * @param roleId 角色id
     * @return boolean
     */
    boolean hasAssignedUsers(String roleId);

    /**
     * <p>
     * 保存用户角色信息
     * </p>
     *
     * @author ciky
     * @since 2026/1/7 15:54
     * @param userId 用户id
     * @param roleIds 角色id列表
     * @return boolean
     */
    boolean saveUserRoles(String userId, List<String> roleIds);
}
