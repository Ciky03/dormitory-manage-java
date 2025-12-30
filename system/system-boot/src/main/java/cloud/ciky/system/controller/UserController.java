package cloud.ciky.system.controller;

import cloud.ciky.base.result.Result;
import cloud.ciky.system.model.dto.UserAuthDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.ciky.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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


    @GetMapping("/test")
    public Result<String> test(){
        String result = userService.test();
        return Result.success(result);
    }

}
