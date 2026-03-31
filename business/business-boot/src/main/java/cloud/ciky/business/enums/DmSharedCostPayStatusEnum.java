package cloud.ciky.business.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * 宿舍费用公摊缴费状态枚举
 *
 * @author ciky
 * @since 2026/3/31 14:05
 */
public enum DmSharedCostPayStatusEnum implements IBaseEnum<Integer> {

    UNPAID(0, "未缴"),
    PAID(1, "已缴");

    @Getter
    private final Integer value;

    @Getter
    private final String label;

    DmSharedCostPayStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
