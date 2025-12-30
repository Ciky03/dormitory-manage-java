package cloud.ciky.system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <p>
 * 系统服务启动类
 * </p>
 *
 * @author ciky
 * @since 2025/12/15 15:56
 */
@SpringBootApplication()
@EnableDiscoveryClient
@MapperScan({"cloud.ciky.system.mapper"})
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
