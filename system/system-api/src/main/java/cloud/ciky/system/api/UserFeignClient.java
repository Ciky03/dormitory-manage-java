package cloud.ciky.system.api;

import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.result.Result;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.core.annotation.RepeatSubmit;
import cloud.ciky.core.config.FeignDecoderConfig;
import cloud.ciky.system.api.fallback.LogFeignFallbackClient;
import cloud.ciky.system.api.fallback.UserFeignFallbackClient;
import cloud.ciky.system.model.dto.UserAuthDTO;
import cloud.ciky.system.model.form.UserForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 * 远程调用系统用户服务
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 17:05
 */
@FeignClient(value = "system", contextId = "user", fallback = UserFeignFallbackClient.class, configuration = {FeignDecoderConfig.class})
public interface UserFeignClient {

    @GetMapping("/user/internal/authInfo/{authKey}")
    Result<UserAuthDTO> getUserAuthInfo(@Parameter(description = "用户名/手机号/邮箱") @PathVariable String authKey);

    @GetMapping("/user/internal/authInfo/wx/mp/{wxMpOpenId}")
    Result<UserAuthDTO> getUserDetailsByWxMpOpenId(@Parameter(description = "微信公众号openId") @PathVariable String wxMpOpenId);

    @PutMapping("/user/internal/bind/wx/mp")
    Result<Void> bindWxMp(@RequestParam String userId, @RequestParam String openId);

    @PostMapping("/user/add")
    Result<Boolean> addUser(@Validated @RequestBody UserForm userForm);


}
