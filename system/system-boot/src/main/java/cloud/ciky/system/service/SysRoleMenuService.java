package cloud.ciky.system.service;

import cloud.ciky.system.model.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色和菜单关联表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-01-04 18:12:09
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * <p>
     * 获取角色的菜单ID集合
     * </p>
     *
     * @author ciky
     * @since 2026/1/6 14:25
     * @param roleId 角色id
     * @return java.util.List<java.lang.String>
     */
    List<String> listMenuIdsByRoleId(String roleId);

    /**
     * <p>
     * 刷新权限缓存(所有角色)
     * </p>
     *
     * @author ciky
     * @since 2026/1/6 15:46
     */
    void refreshRolePermsCache();

    /**
     * <p>
     * 刷新权限缓存(指定角色)
     * </p>
     *
     * @author ciky
     * @since 2026/1/6 16:04
     * @param roleCode 角色编码
     */
    void refreshRolePermsCache(String roleCode);

    /**
     * <p>
     * 刷新权限缓存(修改角色编码时调用)
     * </p>
     *
     * @author ciky
     * @since 2026/1/6 16:03
     * @param oldRoleCode 旧角色编码
     * @param newRoleCode 新角色编码
     */
    void refreshRolePermsCache(String oldRoleCode, String newRoleCode);

    /**
     * <p>
     * 获取角色权限集合
     * </p>
     *
     * @author ciky
     * @since 2026/1/6 16:40
     * @param roles 角色编码集合
     * @return java.util.Set<java.lang.String>
     */
    Set<String> listPermByRoleIds(Set<String> roles);
}
