package cloud.ciky.business.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 楼栋宿管表单
 * </p>
 *
 * @author ciky
 * @since 2026-03-19 23:50
 */
@Data
@Schema(description = "楼栋宿管表单")
public class BuildingDmForm {

    @Schema(description = "楼栋id")
    private String buildingId;

    @Schema(description = "宿管id")
    private String dmId;
}
