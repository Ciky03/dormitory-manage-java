package cloud.ciky.gateway.filter;

import cloud.ciky.base.constant.JwtClaimConstants;
import cloud.ciky.base.constant.RedisConstants;
import cloud.ciky.base.result.ResultCode;
import cloud.ciky.gateway.util.WebFluxUtils;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.jwt.RegisteredPayload;
import com.nimbusds.jose.JWSObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Token 验证全局过滤器
 * </p>
 *
 * @author ciky
 * @since 2025/12/10 11:59
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenGlobalFilter implements GlobalFilter, Ordered {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (CharSequenceUtil.isBlank(authorization) || !CharSequenceUtil.startWithIgnoreCase(authorization, BEARER_PREFIX)) {
            return chain.filter(exchange);
        }

        try {
            String token = authorization.substring(BEARER_PREFIX.length());
            JWSObject jwsObject = JWSObject.parse(token);
            String jti = String.valueOf(jwsObject.getPayload().toJSONObject().get(RegisteredPayload.JWT_ID));
            String userId = String.valueOf(jwsObject.getPayload().toJSONObject().get(JwtClaimConstants.USER_ID));

            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisConstants.Auth.BLACKLIST_TOKEN + jti))) {
                return WebFluxUtils.writeErrorResponse(response, ResultCode.TOKEN_ACCESS_FORBIDDEN);
            }

            if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisConstants.Auth.REPEAT_TOKEN + jti))) {
                redisTemplate.expire(RedisConstants.Auth.REPEAT_TOKEN + jti, 10, TimeUnit.MINUTES);
                return WebFluxUtils.writeErrorResponse(response, ResultCode.USER_ACCOUNT_OTHER_SITE_LOGIN);
            }

            if (Boolean.FALSE.equals(redisTemplate.hasKey(RedisConstants.Auth.LOGIN_TOKEN + userId))) {
                return WebFluxUtils.writeErrorResponse(response, ResultCode.USER_LOGOUT_AUTHORIZATION_ERROR);
            }

        } catch (ParseException e) {
            log.error("在TokenValidationGlobalFilter中解析令牌失败 ", e);
            return WebFluxUtils.writeErrorResponse(response, ResultCode.TOKEN_INVALID);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }

}
