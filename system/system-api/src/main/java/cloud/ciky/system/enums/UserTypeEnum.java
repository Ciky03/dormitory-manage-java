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
    OTHER(0, "其他", "OTHER"),
    STUDENT(1, "学生", "STUDENT"),
    TEACHER(2, "教师", "TEACHER"),
    DORMITORY_MANAGER(3, "宿管", "DORMITORY_MANAGER");

    @Getter
    private Integer value;

    @Getter
    private String label;

    @Getter
    private String code;

    UserTypeEnum(Integer value, String label, String code) {
        this.value = value;
        this.label = label;
        this.code = code;
    }

}
