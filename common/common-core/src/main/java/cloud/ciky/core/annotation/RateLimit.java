package cloud.ciky.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 限流注解
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {

    /**
     * 每秒钟的请求数
     */
    double permitsPerSecond() default 5.0;
}
