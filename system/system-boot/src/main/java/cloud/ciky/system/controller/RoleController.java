package cloud.ciky.system.controller;

import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.model.Option;
import cloud.ciky.base.result.PageResult;
import cloud.ciky.base.result.Result;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.core.annotation.RepeatSubmit;
import cloud.ciky.system.model.form.RoleForm;
import cloud.ciky.system.model.query.RolePageQuery;
import cloud.ciky.system.model.vo.RolePageVO;
import cloud.ciky.system.service.SysRoleMenuService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cloud.ciky.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2025-12-16 15:56:01
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/role")
public class RoleController {

    private final SysRoleService roleService;
    private final SysRoleMenuService roleMenuService;

    @Operation(summary = "角色分页列表")
    @GetMapping("/list/page")
    public PageResult<RolePageVO> getRoleListPage(
            @ParameterObject RolePageQuery query
    ) {
        Page<RolePageVO> result = roleService.getRoleListPage(query);
        return PageResult.success(result);
    }

    @Operation(summary = "角色下拉选项列表")
    @GetMapping("/options")
    public Result<List<Option<String>>> listRoleOptions() {
        List<Option<String>> list = roleService.listRoleOptions();
        return Result.success(list);
    }

    @Operation(summary = "角色表单数据")
    @GetMapping("/form/{roleId}")
    public Result<RoleForm> getRoleForm(
            @Parameter(description = "角色ID") @PathVariable String roleId
    ) {
        RoleForm roleForm = roleService.getRoleForm(roleId);
        return Result.success(roleForm);
    }

    @Operation(summary = "新增角色")
    @Log(value = "新增角色", module = LogModuleEnum.ROLE)
    @PostMapping("/add")
    @RepeatSubmit
    public Result<Void> addRole(@Valid @RequestBody RoleForm roleForm) {
        boolean result = roleService.saveRole(roleForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改角色")
    @Log(value = "修改角色", module = LogModuleEnum.ROLE)
    @PutMapping(value = "/edit/{roleId}")
    @RepeatSubmit
    public Result<Void> updateRole(@PathVariable String roleId,
                                   @Validated @RequestBody RoleForm roleForm) {
        roleForm.setId(roleId);
        boolean result = roleService.saveRole(roleForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改角色状态")
    @Log(value = "修改角色状态", module = LogModuleEnum.ROLE)
    @RepeatSubmit
    @PutMapping(value = "/edit/{roleId}/{status}")
    public Result<Void> updateRoleStatus(
            @Parameter(description = "角色ID") @PathVariable String roleId,
            @Parameter(description = "状态(1:启用;0:禁用)") @PathVariable Boolean status
    ) {
        boolean result = roleService.updateRoleStatus(roleId, status);
        return Result.judge(result);
    }

    @Operation(summary = "删除角色")
    @Log(value = "删除角色", module = LogModuleEnum.ROLE)
    @DeleteMapping("/del/{ids}")
    public Result<Void> deleteRoles(
            @Parameter(description = "删除角色，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = roleService.deleteRoles(ids);
        return Result.judge(result);
    }

    @Operation(summary = "获取角色的菜单ID集合")
    @GetMapping("/menuIds/{roleId}")
    public Result<List<String>> getRoleMenuIds(
            @Parameter(description = "角色ID") @PathVariable String roleId
    ) {
        List<String> menuIds = roleMenuService.listMenuIdsByRoleId(roleId);
        return Result.success(menuIds);
    }

    @Operation(summary = "分配菜单权限给角色")
    @Log(value = "分配菜单权限给角色", module = LogModuleEnum.ROLE)
    @RepeatSubmit
    @PutMapping("/menus/{roleId}")
    @CacheEvict(cacheNames = "menu", key = "'routes'")
    public Result<Void> assignMenusToRole(
            @PathVariable String roleId,
            @RequestBody List<String> menuIds
    ) {
        boolean result = roleService.assignMenusToRole(roleId, menuIds);
        return Result.judge(result);
    }

}
