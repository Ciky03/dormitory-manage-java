package cloud.ciky.gateway.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;

/**
 * <p>
 * Security 客户端配置
 * </p>
 *
 * @author ciky
 * @since 2025/12/10 11:42
 */
@ConfigurationProperties(prefix = "security")
@Configuration(proxyBeanMethods = false)
@EnableWebFluxSecurity
@Slf4j
@Setter
public class SecurityConfig {

    /**
     * 需授权访问的名单请求路径列表
     */
    private List<String> requiredAuthListPaths;


    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        log.info("black-list path:{}", JSONUtil.toJsonStr(requiredAuthListPaths));
        http
                .authorizeExchange(exchange ->
                        {
                            if (CollUtil.isNotEmpty(requiredAuthListPaths)) {
                                exchange.pathMatchers(Convert.toStrArray(requiredAuthListPaths)).authenticated();
                            }
                            exchange.anyExchange().permitAll();
                        }
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

}