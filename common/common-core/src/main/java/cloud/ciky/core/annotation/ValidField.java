package cloud.ciky.core.annotation;

import cloud.ciky.core.validator.FieldValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * <p>
 * 用于验证字段值是否合法的注解
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
@Documented
@Constraint(validatedBy = FieldValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidField {

    /**
     * 验证失败时的错误信息。
     */
    String message() default "非法字段";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 允许的合法值列表。
     */
    String[] allowedValues();

}
