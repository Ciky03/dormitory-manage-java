package cloud.ciky.system.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * <p>
 * 业务用户类型枚举
 * </p>
 *
 * @author ciky
 * @since 2026/3/13 17:38
 */
public enum UserTypeEnum implements IBaseEnum<Integer> {
    OTHER(0, "其他"),
    STUDENT(1, "学生"),
    TEACHER(2, "教师"),
    DORMITORY_MANAGER(3, "宿管");

    @Getter
    private Integer value;

    @Getter
    private String label;

    UserTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

}
