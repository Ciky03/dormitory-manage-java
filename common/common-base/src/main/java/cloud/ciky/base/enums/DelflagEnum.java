package cloud.ciky.base.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * <p>
 * 逻辑删除枚举
 * </p>
 *
 * @author ciky
 * @since 2025/12/12 12:15
 */
@Getter
public enum DelflagEnum implements IBaseEnum<Boolean> {

    REMOVED(Boolean.TRUE, "已删除"),
    USABLE (Boolean.FALSE, "未删除");

    private final Boolean value;

    private final String label;

    DelflagEnum(Boolean value, String label) {
        this.value = value;
        this.label = label;
    }
}
