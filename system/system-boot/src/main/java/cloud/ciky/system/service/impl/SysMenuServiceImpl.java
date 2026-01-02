package cloud.ciky.system.service.impl;

import cloud.ciky.base.constant.SystemConstants;
import cloud.ciky.base.enums.DelflagEnum;
import cloud.ciky.base.enums.StatusEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.base.model.Option;
import cloud.ciky.base.result.ResultCode;
import cloud.ciky.security.util.SecurityUtils;
import cloud.ciky.system.enums.MenuTypeEnum;
import cloud.ciky.system.model.entity.SysMenu;
import cloud.ciky.system.mapper.SysMenuMapper;
import cloud.ciky.system.model.form.MenuForm;
import cloud.ciky.system.model.vo.MenuVO;
import cloud.ciky.system.model.vo.RouteVO;
import cloud.ciky.system.service.SysMenuService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-01-01 17:27:49
 */
@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<MenuVO> listMenus() {
        List<SysMenu> menus = this.list(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getDelflag, DelflagEnum.USABLE.getValue())
                .orderByDesc(SysMenu::getVisible)
                .orderByAsc(SysMenu::getSort, SysMenu::getCreateTime)
        );

        Set<String> parentIds = menus.stream()
                .map(SysMenu::getParentId)
                .collect(Collectors.toSet());

        Set<String> menuIds = menus.stream()
                .map(SysMenu::getId)
                .collect(Collectors.toSet());

        // 获取根节点ID
        List<String> rootIds = parentIds.stream()
                .filter(id -> !menuIds.contains(id))
                .toList();

        // 使用递归函数来构建菜单树
        return rootIds.stream()
                .flatMap(rootId -> buildMenuTree(rootId, menus).stream())
                .toList();
    }

    /**
     * 递归生成菜单列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return 菜单列表
     */
    private List<MenuVO> buildMenuTree(String parentId, List<SysMenu> menuList) {
        return CollUtil.emptyIfNull(menuList)
                .stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(entity -> {
                    MenuVO menuVO = new MenuVO();
                    menuVO.setId(entity.getId());
                    menuVO.setParentId(entity.getParentId());
                    menuVO.setName(entity.getName());
                    menuVO.setType(entity.getType());
                    menuVO.setRouteName(entity.getRouteName());
                    menuVO.setRoutePath(entity.getRoutePath());
                    menuVO.setComponent(entity.getComponent());
                    menuVO.setSort(entity.getSort());
                    menuVO.setVisible(entity.getVisible());
                    menuVO.setPerm(entity.getPerm());
                    List<MenuVO> children = buildMenuTree(entity.getId(), menuList);
                    menuVO.setChildren(children);
                    return menuVO;
                }).toList();
    }

    @Override
    public List<Option<String>> listMenuOptions(boolean onlyParent) {
        List<SysMenu> menuList = this.list(new LambdaQueryWrapper<SysMenu>()
                .in(onlyParent, SysMenu::getType, MenuTypeEnum.CATALOG.getValue(), MenuTypeEnum.MENU.getValue())
                .eq(SysMenu::getDelflag, DelflagEnum.USABLE.getValue())
                .orderByAsc(SysMenu::getSort)
        );
        return buildMenuOptions(SystemConstants.ROOT_NODE_ID, menuList);
    }


    /**
     * 递归生成菜单下拉层级列表（带项目详情标识）
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return 菜单下拉列表
     */
    private List<Option<String>> buildMenuOptions(String parentId, List<SysMenu> menuList) {

        List<Option<String>> menuOptions = new ArrayList<>();

        for (SysMenu menu : menuList) {
            if (menu.getParentId().equals(parentId)) {
                Option<String> option = new Option<>(menu.getId(), menu.getName());
                List<Option<String>> subMenuOptions = buildMenuOptions(menu.getId(), menuList);
                if (!subMenuOptions.isEmpty()) {
                    option.setChildren(subMenuOptions);
                }
                menuOptions.add(option);
            }
        }

        return menuOptions;
    }

    @Override
    public List<RouteVO> listRoutes() {
        Set<String> roles = SecurityUtils.getRoles();

        if (CollUtil.isEmpty(roles)) {
            return Collections.emptyList();
        }

        List<RouteVO> menuList = this.baseMapper.listRoutes(roles);
        return buildRoutes(SystemConstants.ROOT_NODE_ID, menuList);
    }

    /**
     * 递归生成菜单路由层级列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return 路由层级列表
     */
    private List<RouteVO> buildRoutes(String parentId, List<RouteVO> menuList) {
        List<RouteVO> routeList = new ArrayList<>();

        for (RouteVO menu : menuList) {
            if (menu.getParentId().equals(parentId)) {
                String routeName = menu.getRouteName();
                if (CharSequenceUtil.isBlank(routeName)) {
                    // 路由 name 需要驼峰，首字母大写
                    routeName = StringUtils.capitalize(CharSequenceUtil.toCamelCase(menu.getRoutePath(), '/'));
                }
                menu.setRouteName(routeName);
                List<RouteVO> children = buildRoutes(menu.getId(), menuList);
                if (!children.isEmpty()) {
                    menu.setChildren(children);
                }
                routeList.add(menu);
            }
        }

        return routeList;
    }

    @Override
    public MenuForm getMenuForm(String menuId) {
        SysMenu entity = this.getById(menuId);
        if (entity == null) {
            throw new BusinessException("菜单不存在");
        }
        MenuForm menuForm = new MenuForm();
        menuForm.setId(entity.getId());
        menuForm.setParentId(entity.getParentId());
        menuForm.setName(entity.getName());
        menuForm.setType(entity.getType());
        menuForm.setRouteName(entity.getRouteName());
        menuForm.setRoutePath(entity.getRoutePath());
        menuForm.setComponent(entity.getComponent());
        menuForm.setPerm(entity.getPerm());
        menuForm.setVisible(entity.getVisible());
        menuForm.setSort(entity.getSort());
        menuForm.setIcon(CharSequenceUtil.isNotBlank(entity.getIcon()) ? entity.getIcon() : "");
        menuForm.setPitchIcon(CharSequenceUtil.isNotBlank(entity.getPitchIcon()) ? entity.getPitchIcon() : "");
        menuForm.setKeepAlive(entity.getKeepAlive());

        return menuForm;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveMenu(MenuForm menuForm) {
        String menuId = menuForm.getId();
        String parentId = menuForm.getParentId();
        Integer menuType = menuForm.getType();
        String optUser = SecurityUtils.getUserId();

        long count = this.count(new LambdaQueryWrapper<SysMenu>()
                .ne(CharSequenceUtil.isNotBlank(menuId), SysMenu::getId, menuId)
                .and(wrapper ->
                        wrapper.eq(SysMenu::getParentId, parentId)
                                .eq(SysMenu::getName, menuForm.getName())
                                .eq(SysMenu::getDelflag, DelflagEnum.USABLE.getValue())
                ));
        if (count > 0) {
            throw new BusinessException("菜单名称已存在，请修改后重试！");
        }

        if (MenuTypeEnum.CATALOG.getValue().equals(menuType)) {
            // 目录
            String path = menuForm.getRoutePath();
            if (parentId.equals(SystemConstants.ROOT_NODE_ID) && !path.startsWith("/")) {
                menuForm.setRoutePath("/" + path); // 一级目录需以 / 开头
            }
            menuForm.setComponent("Layout");
        } else if (MenuTypeEnum.EXTLINK.getValue().equals(menuType)) {
            // 外链
            menuForm.setComponent(null);
        }

        if (parentId.equals(menuForm.getId())) {
            throw new BusinessException("父级菜单不能为当前菜单");
        }

        SysMenu entity = new SysMenu();
        if (CharSequenceUtil.isBlank(menuId)) {
            entity.setCreateBy(optUser);
        } else {
            if (!SystemConstants.ROOT_NODE_ID.equals(parentId)) {
                SysMenu menu = this.getById(parentId);
                String treePath = menu.getTreePath();
                if (treePath.contains(menuId)) {
                    throw new BusinessException(ResultCode.SYSTEM_OPERATION_DISABLED.getMsg());
                }
            }
            entity.setId(menuId);
            entity.setUpdateBy(optUser);
        }
        entity.setParentId(parentId);
        entity.setName(menuForm.getName());
        entity.setType(menuForm.getType());
        entity.setRouteName(menuForm.getRouteName());
        entity.setRoutePath(menuForm.getRoutePath());
        entity.setComponent(menuForm.getComponent());
        entity.setPerm(menuForm.getPerm());
        entity.setKeepAlive(menuForm.getKeepAlive());
        entity.setVisible(menuForm.getVisible());
        entity.setSort(menuForm.getSort());
        entity.setIcon(menuForm.getIcon());
        entity.setPitchIcon(menuForm.getPitchIcon());
        String treePath = generateMenuTreePath(parentId);
        entity.setTreePath(treePath);

        // 菜单(路由名称唯一)
        if (MenuTypeEnum.MENU.getValue().equals(menuType)) {
            boolean exists = this.exists(new LambdaQueryWrapper<SysMenu>()
                    .ne(CharSequenceUtil.isNotBlank(menuId), SysMenu::getId, menuId)
                    .eq(SysMenu::getRouteName, entity.getRouteName()));
            if (exists) {
                throw new BusinessException("路由名称已存在，请修改后重试！");
            }
        } else {
            // 其他类型时 给路由名称赋值为空
            entity.setRouteName(null);
        }

        boolean result = this.saveOrUpdate(entity);
        //TODO 编辑刷新角色权限缓存
//        if (result && menuForm.getId() != null) {
//            roleMenuService.refreshRolePermsCache();
//            deptMenuService.refreshDeptPermsCache();
//        }
        // 修改菜单如果有子菜单，则更新子菜单的树路径
        updateChildrenTreePath(entity.getId(), treePath);
        return result;
    }

    /**
     * 菜单路径生成
     *
     * @param parentId 父ID
     * @return 父节点路径以英文逗号(, )分割，eg: 1,2,3
     */
    private String generateMenuTreePath(String parentId) {
        if (SystemConstants.ROOT_NODE_ID.equals(parentId)) {
            return parentId;
        } else {
            SysMenu parent = this.getById(parentId);
            return parent != null ? parent.getTreePath() + "," + parent.getId() : null;
        }
    }

    /**
     * 更新子菜单树路径
     *
     * @param id       当前菜单ID
     * @param treePath 当前菜单树路径
     */
    private void updateChildrenTreePath(String id, String treePath) {
        List<SysMenu> children = this.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
        if (CollUtil.isNotEmpty(children)) {
            // 子菜单的树路径等于父菜单的树路径加上父菜单ID
            String childTreePath = treePath + "," + id;
            this.update(new LambdaUpdateWrapper<SysMenu>()
                    .eq(SysMenu::getParentId, id)
                    .set(SysMenu::getTreePath, childTreePath)
            );
            for (SysMenu child : children) {
                // 递归更新子菜单
                updateChildrenTreePath(child.getId(), childTreePath);
            }
        }
    }

    @Override
    public boolean updateMenuVisible(String menuId, Boolean visible) {
        SysMenu menu = this.getById(menuId);
        if (menu == null) {
            throw new BusinessException("菜单不存在");
        }
        if (visible.equals(menu.getVisible())) {
            throw new BusinessException("菜单状态未改变");
        }

        LambdaUpdateWrapper<SysMenu> wrapper = new LambdaUpdateWrapper<SysMenu>()
                .set(SysMenu::getVisible, visible)
                .set(visible, SysMenu::getSort, Math.toIntExact(getSort(menu.getParentId())))
                .set(SysMenu::getUpdateBy, SecurityUtils.getUserId())
                .eq(SysMenu::getId, menuId)
                .or()
                .apply("CONCAT (',',tree_path,',') LIKE CONCAT('%,',{0},',%')", menuId);
        return this.update(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenus(String ids) {
        if (CharSequenceUtil.isBlank(ids)) {
            return false;
        }
        String[] menuIds = ids.split(",");
        for (String menuId : menuIds) {
            SysMenu menu = this.getById(menuId);
            if (menu == null) {
                continue;
            }
            // 判断菜单下是否有子菜单
            long count = this.count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, menuId));
            if (count > 0) {
                throw new BusinessException("菜单【{}】下存在子菜单，请先解除关联后删除", menu.getName());
            }
            boolean removed = this.removeById(menuId);
        }
        // TODO 刷新角色权限缓存
//        roleMenuService.refreshRolePermsCache();
//        deptMenuService.refreshDeptPermsCache();
        return true;
    }


    @Override
    public Long getSort(String parentId) {
        return baseMapper.selectMaxSort(parentId) + 1;
    }
}
