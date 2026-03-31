package cloud.ciky.business.model.form;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 宿舍费用公摊表单对象
 *
 * @author ciky
 * @since 2026-03-31 14:05
 */
@Data
@Schema(description = "宿舍费用公摊表单对象")
public class DmSharedCostForm {

    @Schema(description = "公摊单id")
    private String id;

    @Schema(description = "标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "总金额")
    @NotNull(message = "总金额不能为空")
    private BigDecimal totalAmount;

    @Schema(description = "发生日期")
    @NotNull(message = "发生日期不能为空")
    @JsonFormat(pattern = DateFormatConstants.FORMAT8)
    private LocalDate occurredDate;

    @Schema(description = "截止时间")
    @NotNull(message = "截止时间不能为空")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime dueTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "原始凭证附件id")
    private String sourceVoucherAttachId;

    @Valid
    @Schema(description = "成员分摊列表")
    private List<DmSharedCostMemberForm> memberList;
}
