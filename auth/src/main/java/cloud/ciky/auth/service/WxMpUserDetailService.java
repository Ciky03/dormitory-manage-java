package cloud.ciky.auth.service;

import cloud.ciky.auth.model.SysUserDetails;
import cloud.ciky.base.enums.StatusEnum;
import cloud.ciky.base.result.ResultCode;
import cloud.ciky.system.api.UserFeignClient;
import cloud.ciky.system.model.dto.UserAuthDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * <p>
 * 微信公众号用户认证服务
 * </p>
 *
 * @author ciky
 * @since 2026-01-12 16:20
 */
@Service
@RequiredArgsConstructor
public class WxMpUserDetailService {

    private final UserFeignClient userFeignClient;

    public UserDetails getUserDetailsByWxMpOpenId(String wxMpOpenId){
        UserAuthDTO userAuthInfo = userFeignClient.getUserDetailsByWxMpOpenId(wxMpOpenId).getData();

        if (userAuthInfo == null) {
            throw new BadCredentialsException(ResultCode.USER_NOT_EXIST.getMsg());
        }

        if (!StatusEnum.ENABLE.getValue().equals(userAuthInfo.getStatus())) {
            throw new DisabledException(ResultCode.USER_ACCOUNT_INVALID.getMsg());
        }

        return new SysUserDetails(userAuthInfo);
    }
}
