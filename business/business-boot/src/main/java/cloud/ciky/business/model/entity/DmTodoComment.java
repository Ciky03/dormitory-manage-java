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
 * 宿舍待办评论表
 * </p>
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dm_todo_comment")
public class DmTodoComment extends BaseEntity {

    /**
     * 待办ID
     */
    @TableField("todo_id")
    private String todoId;

    /**
     * 评论人学生ID
     */
    @TableField("commenter_student_id")
    private String commenterStudentId;

    /**
     * 评论内容
     */
    @TableField("content")
    private String content;

}
