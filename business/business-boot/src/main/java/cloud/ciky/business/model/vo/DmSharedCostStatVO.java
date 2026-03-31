package cloud.ciky.business.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 宿舍费用公摊统计视图对象
 *
 * @author ciky
 * @since 2026-03-31 14:05
 */
@Data
@Schema(description = "宿舍费用公摊统计视图对象")
public class DmSharedCostStatVO {

    @Schema(description = "宿舍id")
    private String roomId;

    @Schema(description = "楼栋号")
    private String buildingNum;

    @Schema(description = "宿舍号")
    private String roomNum;

    @Schema(description = "我的未缴总额")
    private BigDecimal myUnpaidAmount;

    @Schema(description = "宿舍未缴总额")
    private BigDecimal roomUnpaidAmount;

    @Schema(description = "本月宿舍未缴总额")
    private BigDecimal currentMonthRoomUnpaidAmount;
}
