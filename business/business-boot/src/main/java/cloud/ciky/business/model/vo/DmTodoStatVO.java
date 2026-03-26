package cloud.ciky.business.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Todo stat view object.
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@Schema(description = "Dorm todo stat view")
public class DmTodoStatVO {

    @Schema(description = "Room id")
    private String roomId;

    @Schema(description = "Building number")
    private String buildingNum;

    @Schema(description = "Room number")
    private String roomNum;

    @Schema(description = "Total count")
    private Integer totalCount;

    @Schema(description = "Pending count")
    private Integer pendingCount;

    @Schema(description = "Processing count")
    private Integer processingCount;

    @Schema(description = "Week completed count")
    private Integer weekCompletedCount;
}
