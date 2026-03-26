package cloud.ciky.business.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 宿舍公约同意情况视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-25 00:31
 */
@Data
@Schema(description = "宿舍公约同意情况视图对象")
public class DmConventionAckStateVO {

    @Schema(description = "同意人数")
    private Integer agreedCount;

    @Schema(description = "总人数")
    private Integer totalCount;

    @Schema(description = "本人同意状态(0-未读 1-已读 2-已同意)")
    private Integer agreeStatus;

    @Schema(description = "宿舍成员同意情况列表")
    private List<DmMemberConventionAckVO> memberAckList;
}

