package cloud.ciky.business.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 * 楼栋/宿舍表单对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-11 17:30
 */
@Data
@Schema(description = "楼栋/宿舍表单对象")
public class DmRoomForm {

    @Schema(description = "id")
    private String id;

    @Schema(description = "父ID")
    @NotBlank(message = "请选择楼栋")
    private String parentId;

    @Schema(description = "父楼栋号")
    private String parentRoomNum;

    @Schema(description = "楼栋号/宿舍号")
    @NotBlank(message = "楼栋号/宿舍号不能为空")
    private String roomNum;

    @Schema(description = "可住人数")
    private Integer capacity;

    @Schema(description = "宿管id")
    private String dmId;

    @Schema(description = "宿管名称")
    private String dmName;

}
