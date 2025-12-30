package cloud.ciky.core.annotation;


import cloud.ciky.base.enums.LogModuleEnum;

import java.lang.annotation.*;

/**
 * <p>
 * 日志注解
 * </p>
 *
 * @author ciky
 * @since 2025/12/9 12:12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Log {

    /**
     * 日志描述
     *
     * @return 日志描述
     */
    String value() default "";

    /**
     * 日志模块
     *
     * @return 日志模块
     */

    LogModuleEnum module();

    /**
     * 是否记录请求参数
     *
     * @return 是否记录请求参数
     */
    boolean params() default true;

    /**
     * 是否记录响应结果
     * <br/>
     * 响应结果默认不记录，避免日志过大
     * @return 是否记录响应结果
     */
    boolean result() default false;

}