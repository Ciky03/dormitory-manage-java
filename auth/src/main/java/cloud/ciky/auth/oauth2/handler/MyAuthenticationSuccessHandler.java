package cloud.ciky.auth.oauth2.handler;

import cloud.ciky.base.constant.RedisConstants;
import cloud.ciky.base.result.Result;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.RegisteredPayload;
import com.nimbusds.jose.JWSObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.DefaultOAuth2AccessTokenResponseMapConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 认证成功处理器
 * </p>
 *
 * @author ciky
 * @since 2025/12/11 17:56
 */
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * MappingJackson2HttpMessageConverter 是 Spring 框架提供的一个 HTTP 消息转换器，用于将 HTTP 请求和响应的 JSON 数据与 Java 对象之间进行转换
     */
    private final HttpMessageConverter<Object> accessTokenHttpResponseConverter = new MappingJackson2HttpMessageConverter();

    /**
     * 把 OAuth2AccessTokenResponse 转成一个 Map<String, Object>
     */
    private final Converter<OAuth2AccessTokenResponse, Map<String, Object>> accessTokenResponseParametersConverter = new DefaultOAuth2AccessTokenResponseMapConverter();

    private final StringRedisTemplate redisTemplate;

    public MyAuthenticationSuccessHandler(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 自定义认证成功响应数据结构
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        try {
            JWSObject jwsObject = JWSObject.parse(accessToken.getTokenValue());
            String uid = String.valueOf(jwsObject.getPayload().toJSONObject().get("userId"));
            String userTokenStr = redisTemplate.opsForValue().get(RedisConstants.Auth.LOGIN_TOKEN + uid);
            JSONObject userToken;
            if (CharSequenceUtil.isNotBlank(userTokenStr)) {
                userToken = JSONUtil.parseObj(userTokenStr);
                // 将旧的token置为重复登录
                JWSObject oldJwsObject = JWSObject.parse(userToken.getJSONObject("access_token").get("tokenValue", String.class));
                String jti = String.valueOf(oldJwsObject.getPayload().toJSONObject().get(RegisteredPayload.JWT_ID));
                redisTemplate.opsForValue().set(RedisConstants.Auth.REPEAT_TOKEN + jti, "重复登录");
            }
            long expiresIn = 86400;
            if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
                expiresIn = ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt());
            }
            userToken = new JSONObject();
            userToken.set("access_token", accessToken);
            userToken.set("refresh_token", refreshToken);
            redisTemplate.opsForValue().set(RedisConstants.Auth.LOGIN_TOKEN + uid, userToken.toString(), expiresIn, TimeUnit.SECONDS);
        } catch (ParseException e) {
            log.error("在MyAuthenticationSuccessHandler中解析令牌失败 ", e);
        }

        OAuth2AccessTokenResponse.Builder builder =
                OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                        .tokenType(accessToken.getTokenType());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }
        OAuth2AccessTokenResponse accessTokenResponse = builder.build();
        Map<String, Object> tokenResponseParameters = this.accessTokenResponseParametersConverter
                .convert(accessTokenResponse);
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

        String clientId = accessTokenAuthentication.getRegisteredClient().getClientId();
        if ("client".equals(clientId)) {
            //  Knife4j测试客户端ID（Knife4j自动填充的 access_token 须原生返回，不能被包装成业务码数据格式）
            this.accessTokenHttpResponseConverter.write(tokenResponseParameters, null, httpResponse);
        } else {
            this.accessTokenHttpResponseConverter.write(Result.success(tokenResponseParameters), null, httpResponse);
        }

    }
}
