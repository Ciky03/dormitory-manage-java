package cloud.ciky.business.model.entity;

import cloud.ciky.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 宿舍待办主表
 * </p>
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dm_todo")
public class DmTodo extends BaseEntity {

    /**
     * 宿舍ID
     */
    @TableField("room_id")
    private String roomId;

    /**
     * 待办标题
     */
    @TableField("title")
    private String title;

    /**
     * 待办内容
     */
    @TableField("content")
    private String content;

    /**
     * 优先级 1-低 2-中 3-高
     */
    @TableField("priority")
    private Boolean priority;

    /**
     * 状态 0-待处理 1-进行中 2-已完成 3-已取消
     */
    @TableField("status")
    private Boolean status;

    /**
     * 负责人学生ID
     */
    @TableField("assignee_student_id")
    private String assigneeStudentId;

    /**
     * 创建人学生ID
     */
    @TableField("creator_student_id")
    private String creatorStudentId;

    /**
     * 截止时间
     */
    @TableField("due_time")
    private LocalDateTime dueTime;

    /**
     * 开始处理时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    @TableField("completed_time")
    private LocalDateTime completedTime;

    /**
     * 完成人学生ID
     */
    @TableField("completed_by")
    private String completedBy;

    /**
     * 取消原因
     */
    @TableField("cancel_reason")
    private String cancelReason;

}
