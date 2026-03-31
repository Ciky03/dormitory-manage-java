package cloud.ciky.business.model.form;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 宿舍待办表单对象
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@Schema(description = "宿舍待办表单对象")
public class DmTodoForm {

    @Schema(description = "待办编号")
    private String id;

    @Schema(description = "待办标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "待办内容")
    @NotBlank(message = "内容不能为空")
    private String content;

    @Schema(description = "优先级")
    @NotNull(message = "优先级不能为空")
    private Integer priority;

    @Schema(description = "负责人学生编号")
    private String assigneeStudentId;

    @Schema(description = "截止时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime dueTime;

    @Schema(description = "取消原因")
    private String cancelReason;
}
