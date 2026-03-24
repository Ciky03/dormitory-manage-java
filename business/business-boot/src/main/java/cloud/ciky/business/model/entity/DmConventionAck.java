package cloud.ciky.business.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>
 * 宿舍公约阅读/同意记录表
 * </p>
 *
 * @author ciky
 * @since 2026-03-24 19:32:07
 */
@Data
@TableName("dm_convention_ack")
public class DmConventionAck implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 公约ID
     */
    @TableField("convention_id")
    private String conventionId;

    /**
     * 学生ID
     */
    @TableField("student_id")
    private String studentId;

    /**
     * 阅读时间
     */
    @TableField("read_time")
    private LocalDateTime readTime;

    /**
     * 是否同意:1-同意 0-未同意
     */
    @TableField("agree_flag")
    private Boolean agreeFlag;

    /**
     * 同意时间
     */
    @TableField("agree_time")
    private LocalDateTime agreeTime;

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
