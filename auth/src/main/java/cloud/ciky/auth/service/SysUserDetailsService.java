package cloud.ciky.auth.service;

import cloud.ciky.auth.model.SysUserDetails;
import cloud.ciky.base.constant.SystemConstants;
import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.enums.StatusEnum;
import cloud.ciky.base.result.ResultCode;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.system.api.UserFeignClient;
import cloud.ciky.system.model.dto.UserAuthDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

/**
 * <p>
 * 系统用户信息加载实现类
 * </p>
 *
 * @author ciky
 * @since 2025/12/12 12:04
 */
@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {

    private final UserFeignClient userFeignClient;

    /**
     * 根据用户名获取用户信息(用户名、密码和角色权限)
     * <p>
     * 用户名、密码用于后续认证，认证成功之后将权限授予用户
     * 支持除用户名外的手机号登录/邮箱
     *
     * @param authKey 用户名/手机号/邮箱
     * @return {@link  SysUserDetails}
     */
    @Log(value = "登录", module = LogModuleEnum.LOGIN)
    @Override
    public UserDetails loadUserByUsername(String authKey) {
        UserAuthDTO userAuthDto = userFeignClient.getUserAuthInfo(authKey).getData();

        if (userAuthDto == null) {
            throw new BadCredentialsException(ResultCode.USER_NOT_EXIST.getMsg());
        }

        if (!StatusEnum.ENABLE.getValue().equals(userAuthDto.getStatus())) {
            throw new DisabledException(ResultCode.USER_ACCOUNT_INVALID.getMsg());
        }

        return new SysUserDetails(userAuthDto);
    }
}
