package cloud.ciky.business.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 宿舍费用公摊缴费表单对象
 *
 * @author ciky
 * @since 2026-03-31 14:05
 */
@Data
@Schema(description = "宿舍费用公摊缴费表单对象")
public class DmSharedCostPayForm {

    @Schema(description = "缴费凭证附件id")
    @NotBlank(message = "请上传缴费凭证")
    private String voucherAttachId;
}
