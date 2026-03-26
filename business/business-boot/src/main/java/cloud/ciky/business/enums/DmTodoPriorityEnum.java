package cloud.ciky.business.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * Todo priority enum.
 *
 * @author ciky
 * @since 2026/3/26 17:31
 */
public enum DmTodoPriorityEnum implements IBaseEnum<Integer> {

    LOW(1, "\u4f4e"),
    MEDIUM(2, "\u4e2d"),
    HIGH(3, "\u9ad8");

    @Getter
    private final Integer value;

    @Getter
    private final String label;

    DmTodoPriorityEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
