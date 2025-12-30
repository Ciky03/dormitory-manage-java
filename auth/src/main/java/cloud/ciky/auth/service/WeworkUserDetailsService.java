package cloud.ciky.auth.service;

import cloud.ciky.auth.model.SysUserDetails;
import cloud.ciky.base.constant.SystemConstants;
import cloud.ciky.base.enums.StatusEnum;
import cloud.ciky.base.result.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 企业微信用户认证服务
 * </p>
 *
 * @author ciky
 * @since 2025/12/12 12:12
 */
@Service
@RequiredArgsConstructor
public class WeworkUserDetailsService {
//
//    private final UserFeignClient userFeignClient;
//
//
    /**
     * 企业微信授权码认证方式
     *
     * @param code 手机号
     * @return 用户信息
     */
    public UserDetails getUserByQyWxCode(String code) {
//        UserAuthInfo userAuthInfo = userFeignClient.getUserAuthInfoQyWxCode(code);
//
//        if (userAuthInfo == null) {
//            throw new BadCredentialsException(ResultCode.USER_NOT_EXIST.getMsg());
//        }
//
//        if (!StatusEnum.ENABLE.getValue().equals(userAuthInfo.getStatus())) {
//            throw new DisabledException(ResultCode.USER_ACCOUNT_INVALID.getMsg());
//        }
//        Set<String> roles = userAuthInfo.getRoles();
//        roles.add("ROLE_".concat(SystemConstants.PLATFORM_ROLE_CODE));
//        userAuthInfo.setRoles(roles);
//
//        return new SysUserDetails(userAuthInfo);
        return null;
    }

}
