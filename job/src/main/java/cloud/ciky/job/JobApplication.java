package cloud.ciky.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>
 * 定时任务服务启动类
 * </p>
 *
 * @author ciky
 * @since 2025/12/15 13:58
 */
@EnableFeignClients(basePackages = "cloud.ciky")
@SpringBootApplication
public class JobApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }

}
