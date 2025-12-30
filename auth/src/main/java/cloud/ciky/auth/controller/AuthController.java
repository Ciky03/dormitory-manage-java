package cloud.ciky.auth.controller;

import cloud.ciky.auth.model.CaptchaResult;
import cloud.ciky.auth.service.AuthService;
import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import cloud.ciky.core.annotation.Log;

/**
 * <p>
 * 认证控制器
 * 登录接口不在此控制器，在过滤器OAuth2TokenEndpointFilter拦截端点(/oauth2/token)处理
 * </p>
 * @author ciky
 * @since 2025/12/11 16:51
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "获取验证码")
    @GetMapping("/captcha")
    @Log(value = "获取验证码", module = LogModuleEnum.LOGIN)
    public Result<CaptchaResult> getCaptcha() {
        CaptchaResult captchaResult = authService.getCaptcha();
        return Result.success(captchaResult);
    }


    @Operation(summary = "发送手机短信验证码")
    @PostMapping("/login/sms/code")
    public Result<Void> sendLoginSmsCode(
            @Parameter(description = "手机号") @RequestParam String mobile
    ) {
        boolean result = authService.sendLoginSmsCode(mobile);
        return Result.judge(result);
    }


    @Operation(summary = "注销")
    @DeleteMapping("/sign/out")
    @Log(value = "注销", module = LogModuleEnum.LOGIN)
    public Result<Void> logout() {
        authService.logout();
        return Result.success();
    }

}
