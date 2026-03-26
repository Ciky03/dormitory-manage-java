package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Todo page view object.
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@Schema(description = "Dorm todo page view")
public class DmTodoPageVO {

    @Schema(description = "Todo id")
    private String id;

    @Schema(description = "Title")
    private String title;

    @Schema(description = "Summary")
    private String summary;

    @Schema(description = "Priority")
    private Integer priority;

    @Schema(description = "Priority label")
    private String priorityLabel;

    @Schema(description = "Status")
    private Integer status;

    @Schema(description = "Status label")
    private String statusLabel;

    @Schema(description = "Assignee student id")
    private String assigneeStudentId;

    @Schema(description = "Assignee name")
    private String assigneeName;

    @Schema(description = "Creator student id")
    private String creatorStudentId;

    @Schema(description = "Creator name")
    private String creatorName;

    @Schema(description = "Due time")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime dueTime;

    @Schema(description = "Create time")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime createTime;

    @Schema(description = "Whether overdue")
    private Boolean overdue;
}
