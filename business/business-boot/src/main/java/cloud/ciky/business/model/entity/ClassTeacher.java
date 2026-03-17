package cloud.ciky.business.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>
 * 班级教师关联表
 * </p>
 *
 * @author ciky
 * @since 2026-03-17 11:25:44
 */
@Data
@TableName("class_teacher")
public class ClassTeacher implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 班级id
     */
    @TableField("class_id")
    private String classId;

    /**
     * 教师id
     */
    @TableField("teacher_id")
    private String teacherId;

    /**
     * 是否当前班级(1-当前 0-历史)
     */
    @TableField("is_current")
    private Boolean isCurrent;

    /**
     * 任职开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 任职结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_By")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
