package cloud.ciky.business.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.format.DecimalStyle;

/**
 * <p>
 * 宿舍公约同意表单对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-25 00:30
 */
@Data
@Schema(description = "宿舍公约同意表单对象")
public class DmConventionAckForm {

    @Schema(description = "宿舍公约id")
    @NotBlank(message = "请选择宿舍公约")
    private String conventionId;

    @Schema(description = "是否同意")
    private Boolean agreeFlag;
}

