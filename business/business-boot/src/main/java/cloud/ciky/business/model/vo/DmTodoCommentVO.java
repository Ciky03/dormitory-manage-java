package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 宿舍待办评论视图对象
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@Schema(description = "宿舍待办评论视图对象")
public class DmTodoCommentVO {

    @Schema(description = "评论编号")
    private String id;

    @Schema(description = "待办编号")
    private String todoId;

    @Schema(description = "评论人学生编号")
    private String commenterStudentId;

    @Schema(description = "评论人姓名")
    private String commenterName;

    @Schema(description = "评论内容")
    private String content;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime createTime;
}
