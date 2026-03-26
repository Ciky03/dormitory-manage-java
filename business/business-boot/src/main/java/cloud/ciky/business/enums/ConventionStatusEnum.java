package cloud.ciky.business.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * <p>
 * 合约状态枚举
 * </p>
 *
 * @author ciky
 * @since 2026-03-25 0:46
 */
public enum ConventionStatusEnum implements IBaseEnum<Integer> {

    DRAFT(0, "草稿"),
    PUBLISHED(1, "已发布"),
    CANCELED(2, "作废");

    @Getter
    private Integer value;

    @Getter
    private String label;

    ConventionStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
