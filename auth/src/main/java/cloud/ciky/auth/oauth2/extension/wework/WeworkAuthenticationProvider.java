package cloud.ciky.auth.oauth2.extension.wework;

import cloud.ciky.auth.service.WeworkUserDetailsService;
import cloud.ciky.auth.util.OAuth2AuthenticationProviderUtils;
import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 企业微信授权认证 Provider
 * </p>
 *
 * @author ciky
 * @since 2025/12/12 12:04
 */
@Slf4j
public class WeworkAuthenticationProvider implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final WeworkUserDetailsService weworkUserDetailsService;


    /**
     * Constructs an {@code OAuth2ResourceOwnerPasswordAuthenticationProviderNew} using the provided parameters.
     *
     * @param authorizationService the authorization service
     * @param tokenGenerator       the token generator
     * @since 0.2.3
     */
    public WeworkAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
            WeworkUserDetailsService weworkUserDetailsService

    ) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        Assert.notNull(weworkUserDetailsService, "weworkUserDetailsService cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.weworkUserDetailsService = weworkUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        WeworkAuthenticationToken wechatAuthenticationToken = (WeworkAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal = OAuth2AuthenticationProviderUtils
                .getAuthenticatedClientElseThrowInvalidClient(wechatAuthenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        if (registeredClient == null) {
            throw new OAuth2AuthenticationException("客户端不存在");
        }

        // 验证客户端是否支持授权类型(grant_type=wework)
        if (!registeredClient.getAuthorizationGrantTypes().contains(WeworkAuthenticationToken.WEWORK)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        // 微信 code 获取 openid
        Map<String, Object> additionalParameters = wechatAuthenticationToken.getAdditionalParameters();
        String code = (String) additionalParameters.get(OAuth2ParameterNames.CODE);
        // 根据 openid 获取会员信息
        // 用户名密码身份验证，成功后返回带有权限的认证信息

        Authentication usernamePasswordAuthentication;
        try {
            UserDetails userDetails = weworkUserDetailsService.getUserByQyWxCode(code);
             usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        } catch (Exception e) {
            // 需要将其他类型的异常转换为 OAuth2AuthenticationException 才能被自定义异常捕获处理，逻辑源码 OAuth2TokenEndpointFilter#doFilterInternal
            throw new OAuth2AuthenticationException(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        }

        // 验证申请访问范围(Scope)
        Set<String> authorizedScopes = registeredClient.getScopes();
        Set<String> requestedScopes = wechatAuthenticationToken.getScopes();
        if (!CollectionUtils.isEmpty(requestedScopes)) {
            Set<String> unauthorizedScopes = requestedScopes.stream()
                    .filter(requestedScope -> !registeredClient.getScopes().contains(requestedScope))
                    .collect(Collectors.toSet());
            if (!CollectionUtils.isEmpty(unauthorizedScopes)) {
                throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
            }
            authorizedScopes = new LinkedHashSet<>(requestedScopes);
        }

        // 访问令牌(Access Token) 构造器
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthentication)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizationGrantType(WeworkAuthenticationToken.WEWORK)
                .authorizedScopes(authorizedScopes)
                .authorizationGrant(wechatAuthenticationToken);

        // 生成访问令牌(Access Token)
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "令牌生成器生成访问令牌失败", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(usernamePasswordAuthentication.getName())
                .authorizationGrantType(WeworkAuthenticationToken.WEWORK)
                .authorizedScopes(authorizedScopes)
                .attribute(Principal.class.getName(), usernamePasswordAuthentication);  // attribute 字段
        if (generatedAccessToken instanceof ClaimAccessor generated) {
            authorizationBuilder.token(accessToken, metadata ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, generated.getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        // 生成刷新令牌(Refresh token)
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "令牌生成器生成刷新令牌失败", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

        // 持久化令牌发放记录到数据库
        OAuth2Authorization authorization = authorizationBuilder.build();
        this.authorizationService.save(authorization);
        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
    }


    /**
     * 判断传入的 authentication 类型是否与当前认证提供者(AuthenticationProvider)相匹配--模板方法
     * <p>
     * ProviderManager#authenticate 遍历 providers 找到支持对应认证请求的 provider-迭代器模式
     *
     * @param authentication 认证请求
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return WeworkAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
