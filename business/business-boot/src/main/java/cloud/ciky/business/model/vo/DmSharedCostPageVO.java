package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 宿舍费用公摊分页视图对象
 *
 * @author ciky
 * @since 2026-03-31 14:05
 */
@Data
@Schema(description = "宿舍费用公摊分页视图对象")
public class DmSharedCostPageVO {

    @Schema(description = "公摊单id")
    private String id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Schema(description = "发起人姓名")
    private String initiatorName;

    @Schema(description = "发生日期")
    @JsonFormat(pattern = DateFormatConstants.FORMAT8)
    private LocalDate occurredDate;

    @Schema(description = "截止时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime dueTime;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "状态文案")
    private String statusLabel;

    @Schema(description = "我的应缴金额")
    private BigDecimal myAmountDue;

    @Schema(description = "我的缴费状态")
    private Integer myPayStatus;

    @Schema(description = "我的缴费状态文案")
    private String myPayStatusLabel;
}
