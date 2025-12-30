package cloud.ciky.datascope.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>
 * 数据权限自动配置类
 * </p>
 *
 * @author ciky
 * @since 2025-12-17 18:24
 */
@AutoConfiguration
@ComponentScan("cloud.ciky.datascope")
@MapperScan("cloud.ciky.datascope.mapper")
public class DataScopeAutoConfiguration {
}
