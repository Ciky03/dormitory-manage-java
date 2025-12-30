package cloud.ciky.system.config;

import cloud.ciky.datascope.handler.MyDataPermissionHandler;
import cloud.ciky.datascope.provider.IDataScopeProvider;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>
 * mybatis-plus 配置类
 * </p>
 *
 * @author ciky
 * @since 2025-12-17 11:41
 */
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {


    /**
     * 分页插件和乐观锁插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(MyDataPermissionHandler handler) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //数据权限插件
        interceptor.addInnerInterceptor(new DataPermissionInterceptor(handler));
        //分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        //乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
