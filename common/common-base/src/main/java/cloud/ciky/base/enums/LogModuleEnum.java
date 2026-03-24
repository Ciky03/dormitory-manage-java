package cloud.ciky.base.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * <p>
 * 日志模块枚举
 * </p>
 *
 * @author ciky
 * @since 2025/12/12 12:15
 */
@Schema(enumAsRef = true)
@Getter
public enum LogModuleEnum {

    EXCEPTION("异常"),
    LOGIN("登录"),
    USER("用户"),
    ROLE("角色"),
    MENU("菜单"),
    DICT("字典"),
    EDUCATION("学院/专业/班级"),
    DORMITORY("楼栋/宿舍"),
    STUDENT("学生"),
    TEACHER("教师"),
    DORMITORY_MANAGER("宿管");

    @JsonValue
    private final String moduleName;

    LogModuleEnum(String moduleName) {
        this.moduleName = moduleName;
    }
}
