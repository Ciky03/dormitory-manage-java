package cloud.ciky.business.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Todo comment form.
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@Schema(description = "Dorm todo comment form")
public class DmTodoCommentForm {

    @Schema(description = "Todo id")
    @NotBlank(message = "\u5f85\u529eid\u4e0d\u80fd\u4e3a\u7a7a")
    private String todoId;

    @Schema(description = "Comment content")
    @NotBlank(message = "\u8bc4\u8bba\u5185\u5bb9\u4e0d\u80fd\u4e3a\u7a7a")
    private String content;
}
