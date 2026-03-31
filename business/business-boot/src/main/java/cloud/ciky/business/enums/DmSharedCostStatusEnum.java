package cloud.ciky.business.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * 宿舍费用公摊状态枚举
 *
 * @author ciky
 * @since 2026/3/31 14:05
 */
public enum DmSharedCostStatusEnum implements IBaseEnum<Integer> {

    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    COMPLETED(2, "已完成"),
    CANCELED(3, "已取消");

    @Getter
    private final Integer value;

    @Getter
    private final String label;

    DmSharedCostStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
