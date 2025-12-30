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
    ALL(0, "所有数据"),
    BUILDING(1, "楼栋"),   //宿管权限: building_id in (...)
    ROOM(2, "宿舍"),      //学生权限: room_id in (...)
    STUDENT(3, "学生"),   //教师权限: student_id in (...)
    NONE(4, "无");       //无权限: 1=0

    private final Integer value;

    private final String label;

    DataScopeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
