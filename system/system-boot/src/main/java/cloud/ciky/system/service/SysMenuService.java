package cloud.ciky.system.service;

import cloud.ciky.base.model.Option;
import cloud.ciky.system.model.entity.SysMenu;
import cloud.ciky.system.model.form.MenuForm;
import cloud.ciky.system.model.vo.MenuVO;
import cloud.ciky.system.model.vo.RouteVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单管理 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-01-01 17:27:49
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * <p>
     * 获取菜单列表
     * </p>
     *
     * @author ciky
     * @since 2026/1/2 11:51
     * @return java.util.List<cloud.ciky.system.model.vo.MenuVO>
     */
    List<MenuVO> listMenus();

    /**
     * <p>
     * 获取菜单下拉列表
     * </p>
     *
     * @author ciky
     * @since 2026/1/2 11:51
     * @param onlyParent 是否只查询父级菜单
     * @return java.util.List<cloud.ciky.base.model.Option<java.lang.String>>
     */
    List<Option<String>> listMenuOptions(boolean onlyParent);

    /**
     * <p>
     * 获取菜单路由列表
     * </p>
     *
     * @author ciky
     * @since 2026/1/2 11:52
     * @return java.util.List<cloud.ciky.system.model.vo.RouteVO>
     */
    List<RouteVO> listRoutes();

    /**
     * <p>
     * 获取菜单表单数据
     * </p>
     *
     * @author ciky
     * @since 2026/1/2 11:52
     * @param menuId 菜单id
     * @return cloud.ciky.system.model.form.MenuForm
     */
    MenuForm getMenuForm(String menuId);

    /**
     * <p>
     * 保存菜单
     * </p>
     *
     * @author ciky
     * @since 2026/1/2 11:52
     * @param menuForm 菜单表单
     * @return boolean
     */
    boolean saveMenu(MenuForm menuForm);

    /**
     * <p>
     * 修改菜单显示状态
     * </p>
     *
     * @author ciky
     * @since 2026/1/2 11:53
     * @param menuId 菜单id
     * @param visible 显示状态(1:显示;0:隐藏)
     * @return boolean
     */
    boolean updateMenuVisible(String menuId, Boolean visible);

    /**
     * <p>
     * 删除菜单
     * </p>
     *
     * @author ciky
     * @since 2026/1/2 11:53
     * @param ids 菜单ID，多个以英文(,)分割
     * @return boolean
     */
    boolean deleteMenus(String ids);

    /**
     * <p>
     * 获取层级最大排序
     * </p>
     *
     * @author ciky
     * @since 2026/1/2 12:25
     * @param parentId 父菜单ID
     * @return java.lang.Long
     */
    Long getSort(String parentId);
}
