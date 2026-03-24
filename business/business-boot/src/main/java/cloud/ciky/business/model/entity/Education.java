package cloud.ciky.business.model.entity;

import cloud.ciky.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 学院/专业/班级表
 * </p>
 *
 * @author ciky
 * @since 2026-02-05 17:03:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("education")
public class Education extends BaseEntity {

    /**
     * 父id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 父路径
     */
    @TableField("tree_path")
    private String treePath;

    /**
     * 类型(1-学院 2-专业 3-班级)
     */
    @TableField("type")
    private Integer type;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 年级(仅班级可用)
     */
    @TableField("grade_year")
    private Integer gradeYear;

}
