package cloud.ciky.auth;

import cloud.ciky.system.api.LogFeignClient;
import cloud.ciky.system.api.UserFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>
 * 认证服务启动类
 * </p>
 *
 * @author ciky
 * @since 2025/12/12 12:15
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackageClasses = {LogFeignClient.class, UserFeignClient.class})
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
