package cloud.ciky.business.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.format.DecimalStyle;

/**
 * <p>
 * 宿舍公约表单对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-25 00:30
 */
@Data
@Schema(description = "宿舍公约表单对象")
public class DmConventionForm {

    @Schema(description = "id")
    private String id;

    @Schema(description = "宿舍id")
    @NotBlank(message = "宿舍不能为空")
    private String roomId;

    @Schema(description = "公约标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "公约内容(Markdown)")
    @NotBlank(message = "公约内容不能为空")
    private String contentMd;
}

