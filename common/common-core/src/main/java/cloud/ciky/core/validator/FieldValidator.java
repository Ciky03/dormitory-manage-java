package cloud.ciky.core.validator;

import cloud.ciky.core.annotation.ValidField;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

/**
 * <p>
 * 字段校验器
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
public class FieldValidator implements ConstraintValidator<ValidField, String> {

    private String[] allowedValues;

    @Override
    public void initialize(ValidField constraintAnnotation) {
        // 初始化允许的值列表
        this.allowedValues = constraintAnnotation.allowedValues();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 如果字段允许为空，可以返回 true
        }
        // 检查值是否在允许列表中
        return Arrays.asList(allowedValues).contains(value);
    }
}
