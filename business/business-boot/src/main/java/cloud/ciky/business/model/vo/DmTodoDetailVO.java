package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Todo detail view object.
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@Schema(description = "Dorm todo detail view")
public class DmTodoDetailVO {

    @Schema(description = "Todo id")
    private String id;

    @Schema(description = "Title")
    private String title;

    @Schema(description = "Content")
    private String content;

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

    @Schema(description = "Start time")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime startTime;

    @Schema(description = "Completed time")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime completedTime;

    @Schema(description = "Completed by")
    private String completedBy;

    @Schema(description = "Completed by name")
    private String completedByName;

    @Schema(description = "Cancel reason")
    private String cancelReason;

    @Schema(description = "Can edit")
    private Boolean canEdit;

    @Schema(description = "Can start")
    private Boolean canStart;

    @Schema(description = "Can complete")
    private Boolean canComplete;

    @Schema(description = "Can cancel")
    private Boolean canCancel;

    @Schema(description = "Comment list")
    private List<DmTodoCommentVO> commentList;
}
