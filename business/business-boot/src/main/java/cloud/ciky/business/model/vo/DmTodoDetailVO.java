package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 宿舍待办详情视图对象
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@Schema(description = "宿舍待办详情视图对象")
public class DmTodoDetailVO {

    @Schema(description = "待办编号")
    private String id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

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

    @Schema(description = "开始时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime startTime;

    @Schema(description = "完成时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime completedTime;

    @Schema(description = "完成人编号")
    private String completedBy;

    @Schema(description = "完成人姓名")
    private String completedByName;

    @Schema(description = "取消原因")
    private String cancelReason;

    @Schema(description = "是否可编辑")
    private Boolean canEdit;

    @Schema(description = "是否可开始")
    private Boolean canStart;

    @Schema(description = "是否可完成")
    private Boolean canComplete;

    @Schema(description = "是否可取消")
    private Boolean canCancel;

    @Schema(description = "评论列表")
    private List<DmTodoCommentVO> commentList;
}
