package cloud.ciky.core.annotation;


import java.lang.annotation.*;

/**
 * <p>
 * 数据权限注解
 * </p>
 *
 * @author ciky
 * @since 2025/12/17 17:36
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface DataPermission {

    /**
     * 主表别名
     */
    String mainAlias() default "";
}
