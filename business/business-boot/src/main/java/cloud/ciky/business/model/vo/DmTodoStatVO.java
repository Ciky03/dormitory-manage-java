package cloud.ciky.business.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 宿舍待办统计视图对象
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@Schema(description = "宿舍待办统计视图对象")
public class DmTodoStatVO {

    @Schema(description = "宿舍编号")
    private String roomId;

    @Schema(description = "楼栋号")
    private String buildingNum;

    @Schema(description = "宿舍号")
    private String roomNum;

    @Schema(description = "待办总数")
    private Integer totalCount;

    @Schema(description = "待处理数量")
    private Integer pendingCount;

    @Schema(description = "进行中数量")
    private Integer processingCount;

    @Schema(description = "本周已完成数量")
    private Integer weekCompletedCount;
}
