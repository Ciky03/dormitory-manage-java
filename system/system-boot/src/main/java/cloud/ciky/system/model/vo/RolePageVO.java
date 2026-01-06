package cloud.ciky.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * <p>
 * 角色分页视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-01-04 17:21
 */
@Data
@Schema(description = "角色分页视图对象")
public class RolePageVO {

    @Schema(description = "角色ID")
    private String id;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色编码")
    private String code;

    @Schema(description = "角色描述")
    private String remark;

    @Schema(description = "角色状态")
    private Boolean status;

    @Schema(description = "显示顺序（数字越小排名越靠前）")
    private Integer sort;

}
