package cloud.ciky.business.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 楼栋/宿舍树查询对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-19 17:04
 */
@Data
@Schema(description = "楼栋/宿舍树查询对象")
public class RoomTreeQuery {

    @Schema(description = "是否查询全部(1-楼栋/宿舍, 0-楼栋)")
    private Boolean queryAll;

    @Schema(description = "宿管id")
    private String dmId;

    @Schema(description = "学生id")
    private String studentId;
}
