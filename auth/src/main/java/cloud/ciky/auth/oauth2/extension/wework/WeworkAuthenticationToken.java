package cloud.ciky.auth.oauth2.extension.wework;

import jakarta.annotation.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 企业微信授权模式身份验证令牌
 * </p>
 *
 * @author ciky
 * @since 2025/12/12 12:08
 */
public class WeworkAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    /**
     * 令牌申请访问范围
     */
    private final Set<String> scopes;

    /**
     * 授权类型：微信小程序
     */
    public static final AuthorizationGrantType WEWORK = new AuthorizationGrantType("wework");


    protected WeworkAuthenticationToken(
            Authentication clientPrincipal,
            Set<String> scopes,
            @Nullable Map<String, Object> additionalParameters
    ) {
        super(WeworkAuthenticationToken.WEWORK, clientPrincipal, additionalParameters);
        this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
    }


    /**
     * 用户凭证(微信小程序 Code)
     */
    @Override
    public Object getCredentials() {
        return this.getAdditionalParameters().get(OAuth2ParameterNames.CODE);
    }

    public Set<String> getScopes() {
        return scopes;
    }

}
