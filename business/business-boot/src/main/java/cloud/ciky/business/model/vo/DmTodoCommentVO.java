package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Todo comment view object.
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@Schema(description = "Dorm todo comment view")
public class DmTodoCommentVO {

    @Schema(description = "Comment id")
    private String id;

    @Schema(description = "Todo id")
    private String todoId;

    @Schema(description = "Commenter student id")
    private String commenterStudentId;

    @Schema(description = "Commenter name")
    private String commenterName;

    @Schema(description = "Content")
    private String content;

    @Schema(description = "Create time")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime createTime;
}
