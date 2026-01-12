
package cloud.ciky.auth.config;

import cloud.ciky.auth.model.SysUserDetails;
import cloud.ciky.auth.oauth2.extension.captcha.CaptchaAuthenticationConverter;
import cloud.ciky.auth.oauth2.extension.captcha.CaptchaAuthenticationProvider;
import cloud.ciky.auth.oauth2.extension.captcha.CaptchaAuthenticationToken;
import cloud.ciky.auth.oauth2.extension.password.PasswordAuthenticationConverter;
import cloud.ciky.auth.oauth2.extension.password.PasswordAuthenticationProvider;
import cloud.ciky.auth.oauth2.extension.smscode.SmsCodeAuthenticationConverter;
import cloud.ciky.auth.oauth2.extension.smscode.SmsCodeAuthenticationProvider;
import cloud.ciky.auth.oauth2.extension.smscode.SmsCodeAuthenticationToken;
import cloud.ciky.auth.oauth2.extension.wechat.WechatAuthenticationConverter;
import cloud.ciky.auth.oauth2.extension.wechat.WechatAuthenticationProvider;
import cloud.ciky.auth.oauth2.extension.wechat.WechatAuthenticationToken;
import cloud.ciky.auth.oauth2.extension.wework.WeworkAuthenticationConverter;
import cloud.ciky.auth.oauth2.extension.wework.WeworkAuthenticationProvider;
import cloud.ciky.auth.oauth2.extension.wework.WeworkAuthenticationToken;
import cloud.ciky.auth.oauth2.extension.wxmp.WxMpAuthenticationConverter;
import cloud.ciky.auth.oauth2.extension.wxmp.WxMpAuthenticationProvider;
import cloud.ciky.auth.oauth2.extension.wxmp.WxMpAuthenticationToken;
import cloud.ciky.auth.oauth2.handler.MyAuthenticationFailureHandler;
import cloud.ciky.auth.oauth2.handler.MyAuthenticationSuccessHandler;
import cloud.ciky.auth.oauth2.jackson.SysUserDeserializer;
import cloud.ciky.auth.oauth2.jackson.SysUserMixin;
import cloud.ciky.auth.oauth2.oidc.CustomOidcAuthenticationConverter;
import cloud.ciky.auth.oauth2.oidc.CustomOidcAuthenticationProvider;
import cloud.ciky.auth.oauth2.oidc.CustomOidcUserInfoService;
import cloud.ciky.auth.service.WeworkUserDetailsService;
import cloud.ciky.auth.service.WxMpUserDetailService;
import cloud.ciky.base.constant.RedisConstants;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.text.CharSequenceUtil;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 授权服务器配置 (核心)
 * </p>
 *
 * @author ciky
 * @since 2025/12/11 17:43
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class AuthorizationServerConfig {

    private final CustomOidcUserInfoService customOidcUserInfoService;

    private final OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer;

    private final WeworkUserDetailsService weworkUserDetailsService;

    private final WxMpUserDetailService wxMpUserDetailService;

    private final StringRedisTemplate redisTemplate;

    private final CodeGenerator codeGenerator;

    private final PasswordEncoder passwordEncoder;

    private final WxMaService wxMaService;


    /**
     * 密码加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * <h2>
     * 配置 OAuth2 授权服务器的安全策略，定义 OAuth2 授权端点、令牌生成、成功/失败响应、OpenID Connect配置、异常处理、jwt验证等。
     * (配置了授权端点的请求转换器和认证提供者，以支持多种授权模式)
     * </h2>
     *
     * @param authenticationManager 用于处理身份验证的组件 #{@link #authenticationManager(AuthenticationConfiguration)}
     * @param authorizationService  OAuth2 授权服务，处理授权相关的业务逻辑 #{@link #authorizationService(JdbcTemplate, RegisteredClientRepository)}
     * @param tokenGenerator        令牌生成器，用于生成访问令牌和刷新令牌 #{@link #tokenGenerator(JWKSource)}
     * @param http                  HttpSecurity 用于配置 HTTP 请求的安全策略
     * @return 返回配置好的 SecurityFilterChain 实例，用于应用的安全过滤器链
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager,
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<?> tokenGenerator
    ) throws Exception {
        // (1) 应用默认的 OAuth2 授权服务器安全配置
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                                // (2) 自定义授权模式转换器(Converter)
                                .accessTokenRequestConverters(authenticationConverters -> authenticationConverters.addAll(
                                                List.of(
                                                        new PasswordAuthenticationConverter(),
                                                        new CaptchaAuthenticationConverter(),
                                                        new WxMpAuthenticationConverter()
//                                        new WeworkAuthenticationConverter(),
//                                        new WechatAuthenticationConverter(),
//                                        new SmsCodeAuthenticationConverter()
                                                ))
                                )
                                // (3) 自定义授权模式提供者(Provider)
                                .authenticationProviders(authenticationProviders -> authenticationProviders.addAll(
                                                List.of(
                                                        new PasswordAuthenticationProvider(authenticationManager, authorizationService, tokenGenerator),
                                                        new CaptchaAuthenticationProvider(authenticationManager, authorizationService, tokenGenerator, redisTemplate, codeGenerator),
                                                        new WxMpAuthenticationProvider(authorizationService, tokenGenerator, redisTemplate, wxMpUserDetailService)
//                                        new WeworkAuthenticationProvider(authorizationService, tokenGenerator, weworkUserDetailsService),
//                                        new WechatAuthenticationProvider(authorizationService, tokenGenerator,  wxMaService),
//                                        new SmsCodeAuthenticationProvider(authorizationService, tokenGenerator, redisTemplate)
                                                ))
                                )
                                // (4) 自定义成功响应
                                .accessTokenResponseHandler(new MyAuthenticationSuccessHandler(redisTemplate))
                                // (5) 自定义失败响应
                                .errorResponseHandler(new MyAuthenticationFailureHandler())
                )
                // (6)启用 OpenID Connect 1.0 自定义
                .oidc(oidcCustomizer ->
                        oidcCustomizer.userInfoEndpoint(userInfoEndpointCustomizer -> {
                            userInfoEndpointCustomizer.userInfoRequestConverter(new CustomOidcAuthenticationConverter(customOidcUserInfoService));
                            userInfoEndpointCustomizer.authenticationProvider(new CustomOidcAuthenticationProvider(authorizationService));
                        })
                );

        http
                // (7) 配置异常处理
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                )
                // (8) 配置 JWT 验证
                .oauth2ResourceServer(oauth2ResourceServer ->
                        oauth2ResourceServer.jwt(Customizer.withDefaults()));
        //http.sessionManagement(httpSecurity -> httpSecurity.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        return http.build();
    }

    /**
     * 创建并配置一个 `RegisteredClientRepository` Bean。
     * `RegisteredClientRepository` 负责存储和检索 OAuth2 客户端的信息。
     * 在此方法中，初始化并将客户端数据保存到数据库中。
     * {@link #initAdminClient}
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
        // 初始化 OAuth2 客户端
        initAdminClient(registeredClientRepository);
        return registeredClientRepository;
    }

    /**
     * 创建并配置一个基于 JDBC 的 `OAuth2AuthorizationService` 实例，该服务负责存储和管理 OAuth2 授权信息。
     * 并通过自定义Mixin进行序列化和反序列化。
     * {@link SysUserMixin}
     * {@link SysUserDeserializer}
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
                                                           RegisteredClientRepository registeredClientRepository) {
        // 创建基于JDBC的OAuth2授权服务。这个服务使用JdbcTemplate和客户端仓库来存储和检索OAuth2授权数据。
        JdbcOAuth2AuthorizationService service = new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);

        // 创建并配置用于处理数据库中OAuth2授权数据的行映射器。
        JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper rowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(registeredClientRepository);
        rowMapper.setLobHandler(new DefaultLobHandler());
        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        // You will need to write the Mixin for your class so Jackson can marshall it.

        // 添加自定义Mixin，用于序列化/反序列化特定的类。
        // Mixin类需要自行实现，以便Jackson可以处理这些类的序列化。
        objectMapper.addMixIn(SysUserDetails.class, SysUserMixin.class);
        objectMapper.addMixIn(Long.class, Object.class);

        // 将配置好的ObjectMapper设置到行映射器中。
        rowMapper.setObjectMapper(objectMapper);

        // 将自定义的行映射器设置到授权服务中。
        service.setAuthorizationRowMapper(rowMapper);

        return service;
    }

    /**
     * 获取默认的 `AuthenticationManager` 实例，并将其注册为 Spring 容器中的 Bean，供其他组件使用。
     * `AuthenticationManager` 是 Spring Security 中的核心组件，负责处理身份验证请求。
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * 该实例用于生成 OAuth2 令牌（访问令牌和刷新令牌）。
     * 这个方法配置了三个令牌生成器：
     * 1. `JwtGenerator` 用于生成和签署 JWT 格式的访问令牌。
     * 2. `OAuth2AccessTokenGenerator` 用于生成 OAuth2 访问令牌。
     * 3. `OAuth2RefreshTokenGenerator` 用于生成 OAuth2 刷新令牌。
     * 形成一个完整的令牌生成器。
     * {@link #jwkSource()}
     */
    @Bean
    OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(jwtCustomizer);

        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    /**
     * 生成 JWT 密钥对（公钥和私钥）
     * (JWK（JSON Web Key）是 JWT 签名和验证中使用的公钥和私钥的标准格式。通过这种方式，公钥可以公开共享，私钥用于签署和验证 JWT。)
     * {@link #generateRsaKey()}
     */
    @Bean // <5>
    @SneakyThrows
    public JWKSource<SecurityContext> jwkSource() {

        // 尝试从Redis中获取JWKSet(JWT密钥对，包含非对称加密的公钥和私钥)
        String jwkSetStr = redisTemplate.opsForValue().get(RedisConstants.Auth.JWK_SET);
        if (CharSequenceUtil.isNotBlank(jwkSetStr)) {
            // 如果存在，解析JWKSet并返回
            JWKSet jwkSet = JWKSet.parse(jwkSetStr);
            return new ImmutableJWKSet<>(jwkSet);
        } else {
            // 如果Redis中不存在JWKSet，生成新的JWKSet
            KeyPair keyPair = generateRsaKey();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            // 构建RSAKey
            RSAKey rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();

            // 构建JWKSet
            JWKSet jwkSet = new JWKSet(rsaKey);

            // 将JWKSet存储在Redis中
            redisTemplate.opsForValue().set(RedisConstants.Auth.JWK_SET, jwkSet.toString(Boolean.FALSE));
            return new ImmutableJWKSet<>(jwkSet);
        }

    }

    /**
     * 生成RSA密钥对
     */
    private static KeyPair generateRsaKey() { // <6>
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }


    /**
     * 用于解析和验证JWT。
     * `JWKSource<SecurityContext>`来获取公钥，用于验证 JWT 的签名。
     * 该方法委托给 `OAuth2AuthorizationServerConfiguration` 的 `jwtDecoder` 方法来创建并返回一个配置好的 `JwtDecoder`。
     * 通过这个 `JwtDecoder`，应用能够解码 JWT 并验证其签名，从而确保 JWT 是由可信的服务签发的，并且没有被篡改。
     * {@link #jwkSource()}
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 授权服务器配置(令牌签发者、获取令牌等端点)
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * 保存和查询用户是否已同意授权某个客户端访问其资源
     * {@link #registeredClientRepository(JdbcTemplate)}
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
                                                                         RegisteredClientRepository registeredClientRepository) {
        // Will be used by the ConsentController
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 初始化创建客户端
     */
    private void initAdminClient(JdbcRegisteredClientRepository registeredClientRepository) {

        String clientId = "admin";
        //123456@#$
        String clientSecret = "5634185aa1fbed624d44d07f8a16de31";
        String clientName = "客户端";

        String encodeSecret = passwordEncoder().encode(clientSecret);

        RegisteredClient registeredMallAdminClient = registeredClientRepository.findByClientId(clientId);
        String id = registeredMallAdminClient != null ? registeredMallAdminClient.getId() : UUID.randomUUID().toString();

        RegisteredClient appClient = RegisteredClient.withId(id)
                .clientId(clientId)
                .clientSecret(encodeSecret)
                .clientName(clientName)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) //使用clientId和clientSecret进行身份验证
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)  //授权码模式
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)       //刷新令牌
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)  //客户端凭证
                .authorizationGrantType(AuthorizationGrantType.PASSWORD) // 密码模式
                .authorizationGrantType(CaptchaAuthenticationToken.CAPTCHA) // 验证码+密码模式
                .authorizationGrantType(WeworkAuthenticationToken.WEWORK) // 企业微信模式
                .authorizationGrantType(WxMpAuthenticationToken.WECHAT_MP)  // 微信公众号模式
                .authorizationGrantType(WechatAuthenticationToken.WECHAT_MINI_APP) // 微信小程序模式
                .authorizationGrantType(SmsCodeAuthenticationToken.SMS_CODE) // 短信验证码模式
                .redirectUri("http://127.0.0.1:9991/authorized")
                //.postLogoutRedirectUri("http://127.0.0.1:9991/logout")
//                .scope(OidcScopes.OPENID)
                //.scope(OidcScopes.PROFILE)
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofDays(1))
                        .refreshTokenTimeToLive(Duration.ofDays(7))
                        .reuseRefreshTokens(false)
                        .build())
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();
        registeredClientRepository.save(appClient);
    }
}
