package cloud.ciky.base.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * <p>
 * 数据权限枚举
 * </p>
 *
 * @author ciky
 * @since 2025/12/9 15:06
 */
@Getter
public enum DataScopeEnum implements IBaseEnum<Integer> {

    /**
     * value 越小，数据权限范围越大
     */
    NONE(0, "无"),       //无权限: 1=0
    BUILDING(1, "楼栋"),   //宿管权限: building_id in (...)
    ROOM(2, "宿舍"),      //学生权限: room_id in (...)
    STUDENT(3, "学生"),   //教师权限: student_id in (...)
    ALL(99, "所有数据");    //管理员权限

    private final Integer value;

    private final String label;

    DataScopeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
