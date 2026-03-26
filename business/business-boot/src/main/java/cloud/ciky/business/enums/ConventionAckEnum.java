package cloud.ciky.business.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * <p>
 * 合约阅读/同意状态枚举
 * </p>
 *
 * @author ciky
 * @since 2026-03-26 0:29
 */
public enum ConventionAckEnum implements IBaseEnum<Integer> {

    UNREAD(0, "未读"),
    READ(1, "已读"),
    AGREE(2, "已同意");

    @Getter
    private Integer value;

    @Getter
    private String label;

    ConventionAckEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

}
