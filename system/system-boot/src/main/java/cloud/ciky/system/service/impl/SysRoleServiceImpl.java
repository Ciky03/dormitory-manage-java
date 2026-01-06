package cloud.ciky.system.service.impl;

import cloud.ciky.base.constant.SystemConstants;
import cloud.ciky.base.enums.DelflagEnum;
import cloud.ciky.base.enums.StatusEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.base.model.Option;
import cloud.ciky.security.util.SecurityUtils;
import cloud.ciky.system.model.entity.SysRole;
import cloud.ciky.system.mapper.SysRoleMapper;
import cloud.ciky.system.model.entity.SysRoleMenu;
import cloud.ciky.system.model.entity.SysUserRole;
import cloud.ciky.system.model.form.RoleForm;
import cloud.ciky.system.model.query.RolePageQuery;
import cloud.ciky.system.model.vo.RolePageVO;
import cloud.ciky.system.service.SysRoleMenuService;
import cloud.ciky.system.service.SysRoleService;
import cloud.ciky.system.service.SysUserRoleService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2025-12-16 15:56:01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysUserRoleService userRoleService;
    private final SysRoleMenuService roleMenuService;

    @Override
    public Page<RolePageVO> getRoleListPage(RolePageQuery query) {
        // 查询数据 非超级管理员不显示超级管理员角色
        query.setIsAdmin(SecurityUtils.isAdmin());
        return this.baseMapper.selectRoleList(new Page<>(query.getPageNum(), query.getPageSize()), query);
    }

    @Override
    public List<Option<String>> listRoleOptions() {
        // 查询数据
        List<SysRole> roleList = this.list(new LambdaQueryWrapper<SysRole>()
                .ne(!SecurityUtils.isAdmin(), SysRole::getCode, SystemConstants.ADMIN_ROLE_CODE)
                .select(SysRole::getId, SysRole::getName)
                .eq(SysRole::getDelflag, DelflagEnum.USABLE.getValue())
                .eq(SysRole::getStatus, StatusEnum.ENABLE.getValue())
                .orderByAsc(SysRole::getSort)
        );

        if (CollUtil.isEmpty(roleList)) {
            return Collections.emptyList();
        }

        // 构造option数组
        return roleList.stream().filter(Objects::nonNull)
                .map(role -> {
                    Option<String> option = new Option<>();
                    option.setValue(role.getId());
                    option.setLabel(role.getName());
                    return option;
                }).toList();
    }

    @Override
    public RoleForm getRoleForm(String roleId) {
        SysRole entity = this.getById(roleId);
        if (entity == null) {
            throw new BusinessException("角色不存在");
        }

        RoleForm roleForm = new RoleForm();
        roleForm.setId(entity.getId());
        roleForm.setName(entity.getName());
        roleForm.setCode(entity.getCode());
        roleForm.setRemark(entity.getRemark());
        roleForm.setSort(entity.getSort());
        roleForm.setDataScope(entity.getDataScope());
        return roleForm;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRole(RoleForm roleForm) {
        String roleId = roleForm.getId();
        long count = this.count(new LambdaQueryWrapper<SysRole>()
                .ne(CharSequenceUtil.isNotBlank(roleId), SysRole::getId, roleId)
                .and(wrapper -> wrapper.eq(SysRole::getName, roleForm.getName())
                        .or()
                        .eq(SysRole::getCode, roleForm.getCode())));
        if (count > 0) {
            throw new BusinessException("角色名称或角色编码已存在，请修改后重试！");
        }

        SysRole oldRole = null;
        SysRole entity = new SysRole();

        String optUser = SecurityUtils.getUserId();
        if (CharSequenceUtil.isBlank(roleId)) {
            oldRole = this.getById(roleId);
            entity.setCreateBy(optUser);
        } else {
            entity.setId(roleId);
            entity.setUpdateBy(optUser);
        }
        entity.setName(roleForm.getName());
        entity.setCode(roleForm.getCode());
        entity.setSort(roleForm.getSort());
        entity.setRemark(roleForm.getRemark());
        entity.setDataScope(roleForm.getDataScope());
        String roleCode = entity.getCode();
        boolean result = this.saveOrUpdate(entity);
        // TODO 判断角色编码是否修改，修改了则刷新权限缓存
//        if (result
//                && (oldRole != null
//                && (!CharSequenceUtil.equals(oldRole.getCode(), roleCode)))) {
//            roleMenuService.refreshRolePermsCache(oldRole.getCode(), roleCode);
//        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRoleStatus(String roleId, Boolean status) {
        SysRole role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }
        if (role.getStatus().equals(status)) {
            throw new BusinessException("角色状态未改变");
        }
        if (role.getCode().equals(SystemConstants.ADMIN_ROLE_CODE)) {
            throw new BusinessException("超级管理员角色状态不允许修改");
        }
        if (status.equals(StatusEnum.DISABLE.getValue())) {
            // 禁用角色，判断角色是否被用户关联
            boolean isRoleAssigned = userRoleService.hasAssignedUsers(roleId);
            if (isRoleAssigned) {
                throw new BusinessException("角色【{}】已分配用户，请先解除关联后禁用", role.getName());
            }
        }

        LambdaUpdateWrapper<SysRole> wrapper = new LambdaUpdateWrapper<SysRole>()
                .set(SysRole::getStatus, status)
                .set(status, SysRole::getSort, Math.toIntExact(getSort()))
                .set(SysRole::getUpdateBy, SecurityUtils.getUserId())
                .eq(SysRole::getId, roleId);

        boolean result = this.update(wrapper);

        // TODO 刷新角色的权限缓存
//        if (result) {
//            roleMenuService.refreshRolePermsCache(role.getCode());
//        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoles(String ids) {
        if (CharSequenceUtil.isBlank(ids)) {
            return false;
        }

        List<String> roleIds = Arrays.stream(ids.split(",")).toList();
        for (String roleId : roleIds) {
            SysRole role = this.getById(roleId);
            if (role == null) {
                continue;
            }
            if (role.getCode().equals(SystemConstants.ADMIN_ROLE_CODE)) {
                throw new BusinessException("超级管理员角色不允许移除");
            }
            // 判断角色是否被用户关联
            boolean isRoleAssigned = userRoleService.hasAssignedUsers(roleId);
            if (isRoleAssigned) {
                throw new BusinessException("角色【{}】已分配用户，请先解除关联后删除", role.getName());
            }

            // 删除角色
            boolean result = this.remove(new LambdaQueryWrapper<SysRole>().eq(SysRole::getId, roleId));
            // 删除用户角色关联
            userRoleService.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId));
            // 删除角色菜单关联
            roleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));

            // TODO 删除成功，刷新权限缓存
//            if (result) {
//                roleMenuService.refreshRolePermsCache(role.getCode());
//            }
        }
        return true;
    }

    @Override
    public Long getSort() {
        return this.baseMapper.selectMaxSort() + 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenusToRole(String roleId, List<String> menuIds) {
        SysRole role = this.getById(roleId);
        if (role == null) {
            throw new BusinessException("角色不存在");
        }

        // 删除角色菜单
        roleMenuService.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, roleId));
        // 新增角色菜单
        String optUser = SecurityUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();
        if (CollUtil.isNotEmpty(menuIds)) {
            List<SysRoleMenu> roleMenus = menuIds.stream()
                    .map(menuId -> {
                        SysRoleMenu roleMenu = new SysRoleMenu();
                        roleMenu.setRoleId(roleId);
                        roleMenu.setMenuId(menuId);
                        roleMenu.setCreateBy(optUser);
                        roleMenu.setCreateTime(now);
                        roleMenu.setDelflag(false);
                        return roleMenu;
                    })
                    .toList();
            roleMenuService.saveBatch(roleMenus);
        }
        // TODO 刷新角色的权限缓存
//        roleMenuService.refreshRolePermsCache(role.getCode());

        return true;
    }
}
