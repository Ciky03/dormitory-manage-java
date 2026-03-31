package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 宿舍费用公摊成员视图对象
 *
 * @author ciky
 * @since 2026-03-31 14:05
 */
@Data
@Schema(description = "宿舍费用公摊成员视图对象")
public class DmSharedCostMemberVO {

    @Schema(description = "明细id")
    private String detailId;

    @Schema(description = "学生id")
    private String studentId;

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "应缴金额")
    private BigDecimal amountDue;

    @Schema(description = "缴费状态")
    private Integer payStatus;

    @Schema(description = "缴费状态文案")
    private String payStatusLabel;

    @Schema(description = "缴费时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime paidTime;

    @Schema(description = "缴费凭证附件id")
    private String voucherAttachId;

    @Schema(description = "缴费凭证名称")
    private String voucherName;

    @Schema(description = "缴费凭证URL")
    private String voucherUrl;

    @JsonIgnore
    private String voucherBucket;

    @JsonIgnore
    private String voucherPath;

    @Schema(description = "是否当前用户")
    private Boolean isCurrentUser;
}
