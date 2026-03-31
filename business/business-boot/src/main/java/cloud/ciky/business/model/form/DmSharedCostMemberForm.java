package cloud.ciky.business.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 宿舍费用公摊成员表单对象
 *
 * @author ciky
 * @since 2026-03-31 14:05
 */
@Data
@Schema(description = "宿舍费用公摊成员表单对象")
public class DmSharedCostMemberForm {

    @Schema(description = "学生id")
    @NotBlank(message = "成员不能为空")
    private String studentId;

    @Schema(description = "应缴金额")
    @NotNull(message = "应缴金额不能为空")
    private BigDecimal amountDue;
}
