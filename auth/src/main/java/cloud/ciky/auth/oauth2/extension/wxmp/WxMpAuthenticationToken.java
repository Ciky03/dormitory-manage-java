package cloud.ciky.auth.oauth2.extension.wxmp;

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
 * 微信公众号授权模式身份验证令牌
 * </p>
 *
 * @author ciky
 * @since 2026-01-12 15:59
 */
public class WxMpAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    /**
     * 令牌申请访问范围
     */
    private final Set<String> scopes;

    /**
     * 授权类型：微信小程序
     */
    public static final AuthorizationGrantType WECHAT_MP = new AuthorizationGrantType("wechatmp");


    protected WxMpAuthenticationToken(Authentication clientPrincipal,
                                      Set<String> scopes,
                                      Map<String, Object> additionalParameters) {
        super(WxMpAuthenticationToken.WECHAT_MP, clientPrincipal, additionalParameters);
        this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
    }

    /**
     * 用户凭证(微信公众号OpenIdToken, 从redis换取openId)
     */
    @Override
    public Object getCredentials() {
        return this.getAdditionalParameters().get(OAuth2ParameterNames.CODE);
    }

    public Set<String> getScopes() {
        return scopes;
    }
}
