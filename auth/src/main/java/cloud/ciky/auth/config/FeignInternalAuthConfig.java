package cloud.ciky.auth.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * feign内部调用配置类
 * </p>
 *
 * @author ciky
 * @since 2026-01-11 0:27
 */
@Configuration
public class FeignInternalAuthConfig {

    @Value("${internal.token}")
    private String internalToken;

    @Bean
    public RequestInterceptor internalAuthInterceptor(){
        // 给feign内部调用的接口加上指定请求头
        return template-> template.header("X-Internal-Token", internalToken);
    }
}
