package cloud.ciky.auth.oauth2.extension.wxmp;

import cloud.ciky.auth.service.WxMpUserDetailService;
import cloud.ciky.auth.util.OAuth2AuthenticationProviderUtils;
import cloud.ciky.base.constant.RedisConstants;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
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

import java.security.Principal;
import java.util.Map;

/**
 * <p>
 * 微信公众号授权认证 Provider
 * </p>
 *
 * @author ciky
 * @since 2026-01-12 15:58
 */
@Slf4j
public class WxMpAuthenticationProvider implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    private final StringRedisTemplate redisTemplate;
    private final WxMpUserDetailService wxMpUserDetailService;


    /**
     * Constructs an {@code OAuth2ResourceOwnerPasswordAuthenticationProviderNew} using the provided parameters.
     *
     * @param authorizationService the authorization service
     * @param tokenGenerator       the token generator
     * @since 0.2.3
     */
    public WxMpAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator,
            StringRedisTemplate redisTemplate,
            WxMpUserDetailService wxMpUserDetailService

    ) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        Assert.notNull(redisTemplate, "redisTemplate cannot be null");
        Assert.notNull(wxMpUserDetailService, "wxMpUserDetailService cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.redisTemplate = redisTemplate;
        this.wxMpUserDetailService = wxMpUserDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        WxMpAuthenticationToken wxMpAuthenticationToken = (WxMpAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal = OAuth2AuthenticationProviderUtils
                .getAuthenticatedClientElseThrowInvalidClient(wxMpAuthenticationToken);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        // 验证客户端是否支持授权类型(grant_type=wechatmp)
        if (!registeredClient.getAuthorizationGrantTypes().contains(WxMpAuthenticationToken.WECHAT_MP)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        // openIdToken 从redis获取 openid
        Map<String, Object> additionalParameters = wxMpAuthenticationToken.getAdditionalParameters();
        String openIdToken = (String) additionalParameters.get(OAuth2ParameterNames.CODE);

        String openId = redisTemplate.opsForValue().get(RedisConstants.WxMp.OPEN_ID_TOKEN + openIdToken);
        if (CharSequenceUtil.isBlank(openId)) {
            throw new OAuth2AuthenticationException("微信扫码登录失败!");
        }

        // 根据 openid 获取会员信息
        Authentication usernamePasswordAuthentication;
        try {
            UserDetails userDetails = wxMpUserDetailService.getUserDetailsByWxMpOpenId(openId);
            usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        } catch (Exception e) {
            // 需要将其他类型的异常转换为 OAuth2AuthenticationException 才能被自定义异常捕获处理，逻辑源码 OAuth2TokenEndpointFilter#doFilterInternal
            throw new OAuth2AuthenticationException(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        }

        // 访问令牌(Access Token) 构造器
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthentication)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizationGrantType(WxMpAuthenticationToken.WECHAT_MP)
                .authorizationGrant(wxMpAuthenticationToken);

        // 生成访问令牌(Access Token)
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    "The token generator failed to generate the access token.", ERROR_URI);
            throw new OAuth2AuthenticationException(error);
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(usernamePasswordAuthentication.getName())
                .authorizationGrantType(WxMpAuthenticationToken.WECHAT_MP)
                .attribute(Principal.class.getName(), usernamePasswordAuthentication);
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
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
                        "The token generator failed to generate the refresh token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

        OAuth2Authorization authorization = authorizationBuilder.build();
        this.authorizationService.save(authorization);
        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WxMpAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
