package cloud.ciky.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 自动缓存注解
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCacheable {

    String key();  // 缓存 key

    int ttl() default 300;  // 缓存时间（秒）
}
