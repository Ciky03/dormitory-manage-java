package cloud.ciky.gateway.util;

import cloud.ciky.base.result.Result;
import cloud.ciky.base.result.ResultCode;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * WebFlux 响应处理器
 * </p>
 *
 * @author ciky
 * @since 2025/12/10 11:58
 */
@Slf4j
public class WebFluxUtils {
    private WebFluxUtils() {
    }

    public static Mono<Void> writeErrorResponse(ServerHttpResponse response, ResultCode resultCode) {
        HttpStatus status = determineHttpStatus(resultCode);
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.getHeaders().setAccessControlAllowOrigin("*");
        response.getHeaders().setCacheControl("no-cache");

        String responseBody = JSONUtil.toJsonStr(Result.failed(resultCode));
        DataBuffer buffer = response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer))
                .doOnError(error -> {
                    DataBufferUtils.release(buffer);
                    log.error("Error writing response: {}", error.getMessage());
                });
    }

    private static HttpStatus determineHttpStatus(ResultCode resultCode) {
        return switch (resultCode) {
            case ACCESS_UNAUTHORIZED, TOKEN_INVALID -> HttpStatus.UNAUTHORIZED;
            case TOKEN_ACCESS_FORBIDDEN -> HttpStatus.FORBIDDEN;
            case USER_ACCOUNT_OTHER_SITE_LOGIN -> HttpStatus.LOCKED;
            default -> HttpStatus.BAD_REQUEST;
        };
    }

    /**
     * 获得客户端 IP
     * <p>
     * 参考 {@link ServletUtil} 的 getClientIP 方法
     *
     * @param exchange         请求
     * @param otherHeaderNames 其它 header 名字的数组
     * @return 客户端 IP
     */
    public static String getClientIp(ServerWebExchange exchange, String... otherHeaderNames) {
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
            headers = ArrayUtil.addAll(headers, otherHeaderNames);
        }
        // 方式一，通过 header 获取
        String ip;
        for (String header : headers) {
            ip = exchange.getRequest().getHeaders().getFirst(header);
            if (!NetUtil.isUnknown(ip)) {
                return NetUtil.getMultistageReverseProxyIp(ip);
            }
        }

        // 方式二，通过 remoteAddress 获取
        InetSocketAddress remoteAddress = exchange.getRequest().getRemoteAddress();
        if (remoteAddress == null) {
            return null;
        }
        ip = remoteAddress.getHostString();
        return NetUtil.getMultistageReverseProxyIp(ip);
    }

    /**
     * 获得请求匹配的 Route 路由
     *
     * @param exchange 请求
     * @return 路由
     */
    public static Route getGatewayRoute(ServerWebExchange exchange) {
        return exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
    }


}
