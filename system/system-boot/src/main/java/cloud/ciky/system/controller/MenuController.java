package cloud.ciky.system.controller;

import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.model.Option;
import cloud.ciky.base.result.Result;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.core.annotation.RepeatSubmit;
import cloud.ciky.system.model.form.MenuForm;
import cloud.ciky.system.model.vo.MenuVO;
import cloud.ciky.system.model.vo.RouteVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cloud.ciky.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 菜单管理 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2026-01-01 17:27:49
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/menu")
public class MenuController {

    private final SysMenuService menuService;

    @Operation(summary = "菜单列表")
    @GetMapping("/list")
    public Result<List<MenuVO>> listMenus() {
        List<MenuVO> menuList = menuService.listMenus();
        return Result.success(menuList);
    }

    @Operation(summary = "菜单下拉列表")
    @GetMapping("/options")
    public Result<List<Option<String>>> listMenuOptions(
            @Parameter(description = "是否只查询父级菜单")
            @RequestParam(required = false, defaultValue = "false") boolean onlyParent
    ) {
        List<Option<String>> menus = menuService.listMenuOptions(onlyParent);
        return Result.success(menus);
    }

    @Operation(summary = "菜单路由列表")
    @GetMapping("/routes")
    public Result<List<RouteVO>> listRoutes() {
        List<RouteVO> routeList = menuService.listRoutes();
        return Result.success(routeList);
    }

    @Operation(summary = "菜单表单数据")
    @GetMapping("/form/{menuId}")
    public Result<MenuForm> getMenuForm(
            @Parameter(description = "菜单ID") @PathVariable String menuId
    ) {
        MenuForm menu = menuService.getMenuForm(menuId);
        return Result.success(menu);
    }

    @Operation(summary = "新增菜单")
    @Log(value = "新增菜单", module = LogModuleEnum.MENU)
    @RepeatSubmit
    @CacheEvict(cacheNames = "menu", key = "'routes'")
    @PostMapping("/add")
    public Result<Void> addMenu(@Validated @RequestBody MenuForm menuForm) {
        boolean result = menuService.saveMenu(menuForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改菜单")
    @Log(value = "修改菜单", module = LogModuleEnum.MENU)
    @RepeatSubmit
    @CacheEvict(cacheNames = "menu", key = "'routes'")
    @PutMapping(value = "/edit/{menuId}")
    public Result<Void> updateMenu(
            @PathVariable String menuId,
            @Validated @RequestBody MenuForm menuForm
    ) {
        menuForm.setId(menuId);
        boolean result = menuService.saveMenu(menuForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改菜单显示状态")
    @Log(value = "修改菜单显示状态", module = LogModuleEnum.MENU)
    @RepeatSubmit
    @PutMapping("/edit/{menuId}/{visible}")
    public Result<Void> updateMenuVisible(
            @Parameter(description = "菜单ID") @PathVariable String menuId,
            @Parameter(description = "显示状态(1:显示;0:隐藏)") @PathVariable Boolean visible

    ) {
        boolean result = menuService.updateMenuVisible(menuId, visible);
        return Result.judge(result);
    }


    @Operation(summary = "删除菜单")
    @Log(value = "删除菜单", module = LogModuleEnum.MENU)
    @DeleteMapping("/del/{ids}")
    @CacheEvict(cacheNames = "menu", key = "'routes'")
    public Result<Void> deleteMenus(
            @Parameter(description = "菜单ID，多个以英文(,)分割") @PathVariable("ids") String ids
    ) {
        boolean result = menuService.deleteMenus(ids);
        return Result.judge(result);
    }
}
