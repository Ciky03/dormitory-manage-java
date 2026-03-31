package cloud.ciky.business.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * 宿舍待办状态枚举
 *
 * @author ciky
 * @since 2026/3/26 17:31
 */
public enum DmTodoStatusEnum implements IBaseEnum<Integer> {

    PENDING(0, "待处理"),
    PROCESSING(1, "进行中"),
    COMPLETED(2, "已完成"),
    CANCELED(3, "已取消");

    @Getter
    private final Integer value;

    @Getter
    private final String label;

    DmTodoStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
