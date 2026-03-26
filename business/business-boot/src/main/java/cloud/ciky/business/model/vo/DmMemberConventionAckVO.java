package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import cloud.ciky.business.service.DmConventionAckService;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * "宿舍成员合约同意情况视图对象"
 * </p>
 *
 * @author ciky
 * @since 2026-03-26 0:26
 */
@Data
@Schema(description = "宿舍成员合约同意情况视图对象")
public class DmMemberConventionAckVO {

    @Schema(description = "宿舍成员id")
    private String studentId;

    @Schema(description = "宿舍成员姓名")
    private String studentName;

    @Schema(description = "同意状态(0-未读 1-已读 2-已同意)")
    private Integer agreeStatus;

    @Schema(description = "是否同意(0-未同意 1-同意)")
    private Boolean agreeFlag;

    @Schema(description = "阅读时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT1)
    private LocalDateTime readTime;

    @Schema(description = "同意时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT1)
    private LocalDateTime agreeTime;
}
