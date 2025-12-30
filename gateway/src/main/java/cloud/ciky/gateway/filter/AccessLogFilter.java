package cloud.ciky.gateway.filter;

import cloud.ciky.base.result.Result;
import cloud.ciky.base.result.ResultCode;
import cloud.ciky.gateway.model.AccessLog;
import cloud.ciky.gateway.util.WebFluxUtils;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.StringUtils;
import jakarta.annotation.Resource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cloud.ciky.base.result.ResultCode.SYSTEM_EXECUTION_ERROR;
import static cn.hutool.core.date.DatePattern.NORM_DATETIME_MS_FORMATTER;

/**
 * <p>
 * 网关的访问日志过滤器
 * </p>
 *
 * @author ciky
 * @since 2025/12/10 12:05
 */
@Slf4j
@Component
public class AccessLogFilter implements GlobalFilter, Ordered {

    @Resource
    private CodecConfigurer codecConfigurer;

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * 打印日志
     *
     * @param gatewayLog 网关日志
     */
    private void writeAccessLog(AccessLog gatewayLog) {
        // 方式一：打印 Logger 后，通过 ELK 进行收集
        // log.info("[writeAccessLog][日志内容：{}]", JsonUtils.toJsonString(gatewayLog));

        // 方式二：调用远程服务，记录到数据库中
        // TODO 暂未实现

        // 方式三：打印到控制台，方便排查错误
        try {
            Map<String, Object> values = buildLogValues(gatewayLog);
            log.info("[writeAccessLog][网关日志：{}]", JSONUtil.toJsonPrettyStr(values));
        } catch (Exception e) {
            log.error("写入日志时发生错误", e);
        }
    }

    private Map<String, Object> buildLogValues(AccessLog gatewayLog) {
        Map<String, Object> values = new java.util.concurrent.ConcurrentHashMap<>(15); // 线程安全的集合
        values.put("userId", StringUtils.isBlank(gatewayLog.getUserId()) ? "" : gatewayLog.getUserId());
        values.put("userType", gatewayLog.getUserType() != null ? gatewayLog.getUserType() : "");
        values.put("routeId", gatewayLog.getRoute() != null ? gatewayLog.getRoute().getId() : "");
        values.put("schema", gatewayLog.getSchema());
        values.put("requestUrl", gatewayLog.getRequestUrl());
        values.put("queryParams", gatewayLog.getQueryParams().toSingleValueMap());
        String requestBody = gatewayLog.getRequestBody();
        values.put("requestBody", StringUtils.isBlank(requestBody) ? "" : requestBody);
        values.put("requestHeaders", JSONUtil.toJsonStr(gatewayLog.getRequestHeaders().toSingleValueMap()));
        values.put("userIp", gatewayLog.getUserIp());
        String responseBody = gatewayLog.getResponseBody();
        values.put("responseBody", StringUtils.isBlank(responseBody) ? "" : responseBody);
        values.put("responseHeaders", gatewayLog.getResponseHeaders() != null ? gatewayLog.getResponseHeaders().toSingleValueMap() : "");
        HttpStatus httpStatus = gatewayLog.getHttpStatus();
        values.put("httpStatus", httpStatus != null ? httpStatus.value() : "");
        LocalDateTime startTime = gatewayLog.getStartTime();
        values.put("startTime", startTime == null ? "" : LocalDateTimeUtil.format(startTime, NORM_DATETIME_MS_FORMATTER));
        LocalDateTime endTime = gatewayLog.getEndTime();
        values.put("endTime", endTime == null ? "" : LocalDateTimeUtil.format(endTime, NORM_DATETIME_MS_FORMATTER));
        values.put("duration", gatewayLog.getDuration() != null ? gatewayLog.getDuration() + " ms" : -1);
        return values;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 将 Request 中可以直接获取到的参数，设置到网关日志
        ServerHttpRequest request = exchange.getRequest();
        // TODO traceId
        AccessLog gatewayLog = new AccessLog();
        gatewayLog.setRoute(WebFluxUtils.getGatewayRoute(exchange));
        gatewayLog.setSchema(request.getURI().getScheme());
        gatewayLog.setRequestMethod(request.getMethod().name());
        gatewayLog.setRequestUrl(request.getURI().toString());
        gatewayLog.setQueryParams(request.getQueryParams());
        gatewayLog.setRequestHeaders(request.getHeaders());
        gatewayLog.setStartTime(LocalDateTime.now());
        gatewayLog.setUserIp(WebFluxUtils.getClientIp(exchange));

        // 继续 filter 过滤
        MediaType mediaType = request.getHeaders().getContentType();
        if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)
                || MediaType.APPLICATION_JSON.isCompatibleWith(mediaType)) { // 适合 JSON 和 Form 提交的请求
            return filterWithRequestBody(exchange, chain, gatewayLog);
        }
        return filterWithoutRequestBody(exchange, chain, gatewayLog);
    }

    /**
     * 参考 {@link ModifyRequestBodyGatewayFilterFactory} 实现
     * <p>
     * 差别主要在于使用 modifiedBody 来读取 Request Body 数据
     * (body只能读取一次,需要缓存并重写request和response)
     */
    private Mono<Void> filterWithRequestBody(ServerWebExchange exchange, GatewayFilterChain chain, AccessLog gatewayLog) {
        // 设置 Request Body 读取时，设置到网关日志
        // 此处 codecConfigurer.getReaders() 的目的，是解决 spring.codec.max-in-memory-size 不生效
        ServerRequest serverRequest = ServerRequest.create(exchange, codecConfigurer.getReaders());
        Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
            gatewayLog.setRequestBody(body);
            return Mono.just(body);
        });

        // 创建 BodyInserter 对象
        BodyInserter<Mono<String>, ReactiveHttpOutputMessage> bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
        // 创建 CachedBodyOutputMessage 对象
        HttpHeaders headers = new HttpHeaders();
        headers.putAll(exchange.getRequest().getHeaders());
        // the new content type will be computed by bodyInserter
        // and then set in the request decorator
        headers.remove(HttpHeaders.CONTENT_LENGTH); // 移除
        CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
        // 通过 BodyInserter 将 Request Body 写入到 CachedBodyOutputMessage 中
        return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
            // 包装 Request，用于缓存 Request Body
            ServerHttpRequest decoratedRequest = requestDecorate(exchange, headers, outputMessage);
            // 包装 Response，用于记录 Response Body
            ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, gatewayLog);
            // 记录普通的
            return chain.filter(exchange.mutate().request(decoratedRequest).response(decoratedResponse).build())
                    .then(Mono.fromRunnable(() -> writeAccessLog(gatewayLog))); // 打印日志

        })).onErrorResume(err -> {
            log.error("处理带请求体的请求时发生错误", err);
            return buildReturnMono(exchange, err);
        }).then();
    }

    private Mono<Void> filterWithoutRequestBody(ServerWebExchange exchange, GatewayFilterChain chain, AccessLog accessLog) {
        // 包装 Response，用于记录 Response Body
        ServerHttpResponseDecorator decoratedResponse = recordResponseLog(exchange, accessLog);
        return chain.filter(exchange.mutate().response(decoratedResponse).build())
                .then(Mono.fromRunnable(() -> writeAccessLog(accessLog))) // 打印日志
                .onErrorResume(err -> {
                    log.error("处理无请求体的请求时发生错误", err);
                    return buildReturnMono(exchange, err);
                }).then();
    }


    /**
     * 记录响应日志
     * 通过 DataBufferFactory 解决响应体分段传输问题。
     */
    private ServerHttpResponseDecorator recordResponseLog(ServerWebExchange exchange, AccessLog gatewayLog) {
        ServerHttpResponse response = exchange.getResponse();
        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                try {
                    if (body instanceof Flux) {
                        DataBufferFactory bufferFactory = response.bufferFactory();
                        // 计算执行时间
                        gatewayLog.setEndTime(LocalDateTime.now());
                        gatewayLog.setDuration((int) (LocalDateTimeUtil.between(gatewayLog.getStartTime(),
                                gatewayLog.getEndTime()).toMillis()));
                        // 设置其它字段
                        gatewayLog.setResponseHeaders(response.getHeaders());
                        gatewayLog.setHttpStatus(HttpStatus.valueOf(Objects.requireNonNull(response.getStatusCode()).value()));

                        // 获取响应类型，如果是 json 就打印
                        String originalResponseContentType = exchange.getAttribute(ServerWebExchangeUtils.ORIGINAL_RESPONSE_CONTENT_TYPE_ATTR);
                        if (StringUtils.isNotBlank(originalResponseContentType)
                                && originalResponseContentType.contains("application/json")) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                                // 设置 response body 到网关日志
                                byte[] content = readContent(dataBuffers);
                                String responseResult = new String(content, StandardCharsets.UTF_8);
                                gatewayLog.setResponseBody(responseResult);

                                // 响应
                                return bufferFactory.wrap(content);
                            }));
                        }
                    }
                    // if body is not a flux. never got there.
                    return super.writeWith(body);
                } catch (Exception err) {
                    log.error("记录响应日志时发生错误", err);
                    return buildReturnMono(exchange, err);
                }
            }
        };
    }

    // ========== 参考 ModifyRequestBodyGatewayFilterFactory 中的方法 ==========

    /**
     * 请求装饰器，支持重新计算 headers、body 缓存
     *
     * @param exchange      请求
     * @param headers       请求头
     * @param outputMessage body 缓存
     * @return 请求装饰器
     */
    private ServerHttpRequestDecorator requestDecorate(ServerWebExchange exchange, HttpHeaders headers, CachedBodyOutputMessage outputMessage) {
        return new ServerHttpRequestDecorator(exchange.getRequest()) {
            @NonNull
            @Override
            public HttpHeaders getHeaders() {
                long contentLength = headers.getContentLength();
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());
                if (contentLength > 0) {
                    httpHeaders.setContentLength(contentLength);
                } else {
                    // TODO: this causes a 'HTTP/1.1 411 Length Required' // on
                    // httpbin.org
                    httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                }
                return httpHeaders;
            }

            @NonNull
            @Override
            public Flux<DataBuffer> getBody() {
                return outputMessage.getBody();
            }
        };
    }

    // ========== 参考 ModifyResponseBodyGatewayFilterFactory 中的方法 ==========

    private byte[] readContent(List<? extends DataBuffer> dataBuffers) {
        // 合并多个流集合，解决返回体分段传输
        DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
        DataBuffer join = dataBufferFactory.join(dataBuffers);
        byte[] content = new byte[join.readableByteCount()];
        join.read(content);
        // 释放掉内存
        DataBufferUtils.release(join);
        return content;
    }

    private Mono<Void> buildReturnMono(ServerWebExchange exchange, Throwable th) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String errStr = SYSTEM_EXECUTION_ERROR.getMsg();
        if (th instanceof org.springframework.cloud.gateway.support.NotFoundException ex) {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            errStr = ex.getReason();
        }
        String jsonStr = JSON.toJSONString(Result.failed(ResultCode.SERVICE_UNAVAILABLE, errStr));
        byte[] bits = jsonStr.getBytes(StandardCharsets.UTF_8);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bits)));
    }
}
