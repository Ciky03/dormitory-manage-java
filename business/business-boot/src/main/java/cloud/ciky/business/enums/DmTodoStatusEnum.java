package cloud.ciky.business.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * Todo status enum.
 *
 * @author ciky
 * @since 2026/3/26 17:31
 */
public enum DmTodoStatusEnum implements IBaseEnum<Integer> {

    PENDING(0, "\u5f85\u5904\u7406"),
    PROCESSING(1, "\u8fdb\u884c\u4e2d"),
    COMPLETED(2, "\u5df2\u5b8c\u6210"),
    CANCELED(3, "\u5df2\u53d6\u6d88");

    @Getter
    private final Integer value;

    @Getter
    private final String label;

    DmTodoStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
