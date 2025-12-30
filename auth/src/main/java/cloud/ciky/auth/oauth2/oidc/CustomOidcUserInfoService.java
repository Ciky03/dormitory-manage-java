package cloud.ciky.auth.oauth2.oidc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 自定义 OIDC 用户信息服务
 * </p>
 *
 * @author ciky
 * @since 2025/12/15 11:45
 */
@Service
@Slf4j
public class CustomOidcUserInfoService {

//    private final UserFeignClient userFeignClient;
//
//    public CustomOidcUserInfoService(UserFeignClient userFeignClient) {
//        this.userFeignClient = userFeignClient;
//    }

    public CustomOidcUserInfo loadUserByUsername(String username) {
//        UserAuthInfo userAuthInfo = null;
//        try {
//            userAuthInfo = userFeignClient.getUserAuthInfo(username);
//            if (userAuthInfo == null) {
//                return null;
//            }
//            return new CustomOidcUserInfo(createUser(userAuthInfo));
//        } catch (Exception e) {
//            log.error("获取用户信息失败", e);
//            return null;
//        }
        return null;
    }

//    private Map<String, Object> createUser(UserAuthInfo userAuthInfo) {
//        return CustomOidcUserInfo.customBuilder()
//                .username(userAuthInfo.getUsername())
//                .nickname(userAuthInfo.getNickname())
//                .status(userAuthInfo.getStatus())
//                .phoneNumber(userAuthInfo.getMobile())
//                .email(userAuthInfo.getEmail())
//                .profile(userAuthInfo.getAvatar())
//                .build()
//                .getClaims();
//    }

}
