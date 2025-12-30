package cloud.ciky.auth.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;

/**
 * <p>
 * OAuth2 认证 Provider 辅助工具类
 * 用于：
 *   1. 从 Authentication 中解析已认证的客户端
 *   2. 统一处理客户端校验失败的异常
 *   3. 失效（invalidate）AccessToken / RefreshToken / AuthorizationCode
 * </p>
 *
 * @author ciky
 * @since 2025/12/11 17:17
 */
public class OAuth2AuthenticationProviderUtils {

    /**
     * 工具类禁止实例化
     */
    private OAuth2AuthenticationProviderUtils() {
    }

    /**
     * 从 Authentication 中解析出已认证的客户端（Client）
     * 如果没有认证或类型不匹配，则抛出 INVALID_CLIENT 错误
     */
    public static OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }


    /**
     * 将指定 Token 标记为“失效（INVALIDATED）”
     * 并在 RefreshToken 失效时：
     *   1. 级联失效 AccessToken
     *   2. 级联失效 AuthorizationCode（如果还未失效）
     * 返回更新后的 OAuth2Authorization（包含新的 token metadata）
     */
    public static <T extends OAuth2Token> OAuth2Authorization invalidate(
            OAuth2Authorization authorization, T token) {

        // 把当前 token 标记为失效
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.from(authorization)
                .token(token,
                        metadata ->
                                metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true));

        //如果失效的是 RefreshToken，触发级联失效
        if (OAuth2RefreshToken.class.isAssignableFrom(token.getClass())) {
            //AccessToken 必须立即失效
            authorizationBuilder.token(
                    authorization.getAccessToken().getToken(),
                    metadata ->
                            metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true));

            //未使用的 Authorization Code 也要失效
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                    authorization.getToken(OAuth2AuthorizationCode.class);
            if (authorizationCode != null && !authorizationCode.isInvalidated()) {
                authorizationBuilder.token(
                        authorizationCode.getToken(),
                        metadata ->
                                metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, true));
            }
        }

        //构建新的 Authorization
        return authorizationBuilder.build();
    }
}
