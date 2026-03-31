package cloud.ciky.business.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 宿舍待办评论表单对象
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@Schema(description = "宿舍待办评论表单对象")
public class DmTodoCommentForm {

    @Schema(description = "待办编号")
    @NotBlank(message = "待办id不能为空")
    private String todoId;

    @Schema(description = "评论内容")
    @NotBlank(message = "评论内容不能为空")
    private String content;
}
