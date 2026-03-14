package cloud.ciky.business;

import cloud.ciky.system.api.UserFeignClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>
 * 业务服务启动类
 * </p>
 *
 * @author ciky
 * @since 2026-02-03 17:41
 */
@SpringBootApplication()
@EnableDiscoveryClient
@MapperScan({"cloud.ciky.business.mapper"})
@EnableFeignClients(basePackageClasses = {UserFeignClient.class})
public class BusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(BusinessApplication.class, args);
    }

}
