package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 宿舍费用公摊详情视图对象
 *
 * @author ciky
 * @since 2026-03-31 14:05
 */
@Data
@Schema(description = "宿舍费用公摊详情视图对象")
public class DmSharedCostDetailVO {

    @Schema(description = "公摊单id")
    private String id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "总金额")
    private BigDecimal totalAmount;

    @Schema(description = "发起人学生id")
    private String initiatorStudentId;

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

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "原始凭证附件id")
    private String sourceVoucherAttachId;

    @Schema(description = "原始凭证附件名称")
    private String sourceVoucherName;

    @Schema(description = "原始凭证URL")
    private String sourceVoucherUrl;

    @JsonIgnore
    private String sourceVoucherBucket;

    @JsonIgnore
    private String sourceVoucherPath;

    @Schema(description = "是否可编辑")
    private Boolean canEdit;

    @Schema(description = "是否可发布")
    private Boolean canPublish;

    @Schema(description = "是否可缴费")
    private Boolean canPay;

    @Schema(description = "是否可取消")
    private Boolean canCancel;

    @Schema(description = "成员分摊列表")
    private List<DmSharedCostMemberVO> memberList;
}
