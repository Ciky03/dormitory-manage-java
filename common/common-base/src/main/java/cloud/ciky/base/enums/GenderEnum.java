package cloud.ciky.base.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * <p>
 * 性别枚举
 * </p>
 *
 * @author ciky
 * @since 2025/12/12 12:15
 */
@Getter
public enum GenderEnum implements IBaseEnum<Integer> {

    MALE(1, "男"),
    FEMALE(2, "女"),
    UNKNOWN(0, "未知");

    // @EnumValue //  Mybatis-Plus 提供注解表示插入数据库时插入该值
    private final Integer value;

    // @JsonValue //  表示对枚举序列化时返回此字段
    private final String label;

    GenderEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
