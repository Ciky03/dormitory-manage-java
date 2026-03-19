package cloud.ciky.business.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>
 * 学生宿舍关联表
 * </p>
 *
 * @author ciky
 * @since 2026-03-19 16:43:50
 */
@Data
@TableName("room_student")
public class RoomStudent implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 宿舍id
     */
    @TableField("room_id")
    private String roomId;

    /**
     * 学生id
     */
    @TableField("student_id")
    private String studentId;

    /**
     * 是否当前宿舍(1-当前 0-历史)
     */
    @TableField("is_current")
    private Boolean isCurrent;

    /**
     * 入住时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 退宿时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
