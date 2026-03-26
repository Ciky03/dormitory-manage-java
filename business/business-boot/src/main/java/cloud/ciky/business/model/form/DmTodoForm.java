package cloud.ciky.business.model.form;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Todo save form.
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@Schema(description = "Dorm todo form")
public class DmTodoForm {

    @Schema(description = "Todo id")
    private String id;

    @Schema(description = "Todo title")
    @NotBlank(message = "\u6807\u9898\u4e0d\u80fd\u4e3a\u7a7a")
    private String title;

    @Schema(description = "Todo content")
    @NotBlank(message = "\u5185\u5bb9\u4e0d\u80fd\u4e3a\u7a7a")
    private String content;

    @Schema(description = "Priority")
    @NotNull(message = "\u4f18\u5148\u7ea7\u4e0d\u80fd\u4e3a\u7a7a")
    private Integer priority;

    @Schema(description = "Assignee student id")
    private String assigneeStudentId;

    @Schema(description = "Due time")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime dueTime;

    @Schema(description = "Cancel reason")
    private String cancelReason;
}
