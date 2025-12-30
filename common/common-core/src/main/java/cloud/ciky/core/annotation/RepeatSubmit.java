package cloud.ciky.core.annotation;


import java.lang.annotation.*;

/**
 * <p>
 * 防重提交注解
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RepeatSubmit {

    /**
     * 防重提交锁过期时间(秒)
     * <p>
     * 默认3秒内不允许重复提交
     */
    int expire() default 3;

}
