package cloud.ciky.business.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * 宿舍待办优先级枚举
 *
 * @author ciky
 * @since 2026/3/26 17:31
 */
public enum DmTodoPriorityEnum implements IBaseEnum<Integer> {

    LOW(1, "低"),
    MEDIUM(2, "中"),
    HIGH(3, "高");

    @Getter
    private final Integer value;

    @Getter
    private final String label;

    DmTodoPriorityEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
