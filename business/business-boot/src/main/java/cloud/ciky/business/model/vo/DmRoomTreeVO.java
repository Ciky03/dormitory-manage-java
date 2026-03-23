package cloud.ciky.business.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 楼栋/宿舍树视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-19 17:04
 */
@Data
@Schema(description = "楼栋/宿舍树视图对象")
public class DmRoomTreeVO {

    @Schema(description = "id")
    private String id;

    @Schema(description = "父id")
    private String parentId;

    @Schema(description = "父路径")
    private String treePath;

    @Schema(description = "楼栋/宿舍号")
    private String roomNum;

    @Schema(description = "可住人数")
    private Integer capacity;

    @Schema(description = "是否已选择(1-已选择 2-未选择)")
    private Boolean selected;

    @Schema(description = "宿管id")
    private String dmId;

    @Schema(description = "宿管")
    private String dmName;

    @Schema(description = "子节点")
    private List<DmRoomTreeVO> children;
}
