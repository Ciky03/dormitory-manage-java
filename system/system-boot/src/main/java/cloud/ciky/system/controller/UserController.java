package cloud.ciky.system.controller;

import cloud.ciky.base.BaseQuery;
import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.model.Option;
import cloud.ciky.base.result.PageResult;
import cloud.ciky.base.result.Result;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.core.annotation.RepeatSubmit;
import cloud.ciky.security.util.SecurityUtils;
import cloud.ciky.system.model.dto.UserAuthDTO;
import cloud.ciky.system.model.form.PwdUpdateForm;
import cloud.ciky.system.model.form.UserForm;
import cloud.ciky.system.model.query.UserPageVO;
import cloud.ciky.system.model.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cloud.ciky.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 *  用户表 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 16:10:58
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final SysUserService userService;

    @Operation(summary = "获取用户认证信息", hidden = true)
    @GetMapping("/authInfo/{authKey}")
    public Result<UserAuthDTO> getUserAuthInfo(
            @Parameter(description = "用户名/手机号/邮箱") @PathVariable String authKey
    ) {
        UserAuthDTO userAuthInfo = userService.getUserAuthInfo(authKey);
        return Result.success(userAuthInfo);
    }

    @Operation(summary = "获取登录用户信息")
    @GetMapping("/current")
    public Result<UserInfoVO> getCurrentUserInfo() {
        UserInfoVO userInfoVO = userService.getCurrentUserInfo();
        return Result.success(userInfoVO);
    }

    @Operation(summary = "用户分页列表")
    @GetMapping("/list/page")
    public PageResult<UserPageVO> getUserListPage(
            @ParameterObject BaseQuery query
    ) {
        Page<UserPageVO> result = userService.getUserListPage(query);
        return PageResult.success(result);
    }

    @Operation(summary = "用户下拉选项")
    @GetMapping("/options")
    public Result<List<Option<String>>> listUserOptions() {
        List<Option<String>> list = userService.listUserOptions();
        return Result.success(list);
    }

    @Operation(summary = "用户表单数据")
    @GetMapping("/form/{userId}")
    public Result<UserForm> getUserForm(
            @Parameter(description = "用户ID") @PathVariable String userId
    ) {
        UserForm formData = userService.getUserForm(userId);
        return Result.success(formData);
    }

    @Operation(summary = "新增用户")
    @Log(value = "新增用户", module = LogModuleEnum.USER)
    @PreAuthorize("@ss.hasPerm('sys:user:add')")
    @RepeatSubmit
    @PostMapping("/add")
    public Result<String> addUser(
            @Validated @RequestBody UserForm userForm
    ) {
        String result = userService.saveUser(userForm);
        return Result.success(result);
    }

    @Operation(summary = "修改用户")
    @Log(value = "修改用户", module = LogModuleEnum.USER)
    @PreAuthorize("@ss.hasPerm('sys:user:edit')")
    @RepeatSubmit
    @PutMapping(value = "/edit/{userId}")
    public Result<String> updateUser(
            @Parameter(description = "用户ID") @PathVariable String userId,
            @RequestBody @Validated UserForm userForm) {
        userForm.setId(userId);
        String result = userService.saveUser(userForm);
        return Result.success(result);
    }

    @Operation(summary = "修改用户状态")
    @Log(value = "修改用户状态", module = LogModuleEnum.USER)
    @RepeatSubmit
    @PreAuthorize("@ss.hasPerm('sys:user:edit')")
    @PutMapping(value = "/edit/{userId}/{status}")
    public Result<Void> updateUserStatus(
            @Parameter(description = "用户ID") @PathVariable String userId,
            @Parameter(description = "用户状态(1:启用;0:禁用)") @PathVariable Boolean status
    ) {
        boolean result = userService.updateUserStatus(userId, status);
        return Result.judge(result);
    }

    @Operation(summary = "删除用户")
    @Log(value = "删除用户", module = LogModuleEnum.USER)
    @DeleteMapping("/del/{ids}")
    @PreAuthorize("@ss.hasPerm('sys:user:delete')")
    public Result<Void> deleteUsers(
            @Parameter(description = "用户ID，多个以英文逗号(,)分割") @PathVariable String ids
    ) {
        boolean result = userService.deleteUsers(ids);
        return Result.judge(result);
    }

    @Operation(summary = "重置用户密码")
    @Log(value = "重置用户密码", module = LogModuleEnum.USER)
    @PutMapping(value = "/pwd/reset/{userId}")
    @RepeatSubmit
    @PreAuthorize("@ss.hasPerm('sys:user:password:reset')")
    public Result<String> resetPassword(
            @Parameter(description = "用户ID") @PathVariable String userId
    ) {
        String result = userService.resetPassword(userId);
        return Result.success(result);
    }

    @Operation(summary = "修改密码")
    @Log(value = "修改密码", module = LogModuleEnum.USER)
    @PutMapping(value = "/pwd/edit")
    @RepeatSubmit
    public Result<String> updatePassword(
            @Validated @RequestBody PwdUpdateForm pwdForm
    ) {
        String optUser = SecurityUtils.getUserId();
        boolean result = userService.changePassword(optUser, pwdForm);
        return Result.judge(result);
    }

    @Operation(summary = "修改头像")
    @Log(value = "修改头像", module = LogModuleEnum.USER)
    @PutMapping(value = "/avatar/edit")
    @RepeatSubmit
    public Result<String> updateAvatar(
            @Validated @RequestParam String attachId
    ) {
        String optUser = SecurityUtils.getUserId();
        boolean result = userService.changeAvatar(optUser, attachId);
        return Result.judge(result);
    }

}
