package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 宿舍待办分页视图对象
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@Schema(description = "宿舍待办分页视图对象")
public class DmTodoPageVO {

    @Schema(description = "待办编号")
    private String id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "摘要")
    private String summary;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "优先级文案")
    private String priorityLabel;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "状态文案")
    private String statusLabel;

    @Schema(description = "负责人学生编号")
    private String assigneeStudentId;

    @Schema(description = "负责人姓名")
    private String assigneeName;

    @Schema(description = "创建人学生编号")
    private String creatorStudentId;

    @Schema(description = "创建人姓名")
    private String creatorName;

    @Schema(description = "截止时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime dueTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime createTime;

    @Schema(description = "是否逾期")
    private Boolean overdue;
}
