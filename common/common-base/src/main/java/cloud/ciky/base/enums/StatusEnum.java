package cloud.ciky.base.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * <p>
 * 状态枚举
 * </p>
 *
 * @author ciky
 * @since 2025/12/12 12:15
 */
@Getter
public enum StatusEnum implements IBaseEnum<Boolean> {

    ENABLE(Boolean.TRUE, "启用"),
    DISABLE (Boolean.FALSE, "禁用");

    private final Boolean value;

    private final String label;

    StatusEnum(Boolean value, String label) {
        this.value = value;
        this.label = label;
    }
}
