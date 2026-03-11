package cloud.ciky.business.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * <p>
 * 学院/专业/班级类型枚举
 * </p>
 *
 * @author ciky
 * @since 2026/2/5 18:04
 */
public enum EduUnitTypeEnum implements IBaseEnum<Integer> {
    COLLEGE(1, "学院"),
    MAJOR(2, "专业"),
    CLASS(3, "班级");


    @Getter
    private Integer value;

    @Getter
    private String label;

    EduUnitTypeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

}
