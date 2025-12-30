package cloud.ciky.base.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;


/**
 * <p>
 * 角色枚举
 * </p>
 *
 * @author ciky
 * @since 2025/12/16 14:26
 */
@Getter
public enum RoleEnum implements IBaseEnum<String> {

    ADMIN("ADMIN", "系统管理员"),
    DORM_MANAGER("DORM_MANAGER", "宿管"),
    TEACHER("TEACHER", "教师"),
    STUDENT("STUDENT", "学生");


    private final String value;

    private final String label;

    RoleEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }


}
