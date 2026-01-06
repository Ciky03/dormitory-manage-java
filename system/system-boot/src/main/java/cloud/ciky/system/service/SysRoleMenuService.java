package cloud.ciky.system.service;

import cloud.ciky.system.model.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

}
