package cloud.ciky.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 菜单视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-01-02 11:39
 */
@Data
@Schema(description = "菜单视图对象")
public class MenuVO {

    @Schema(description = "菜单ID")
    private String id;

    @Schema(description = "父菜单ID")
    private String parentId;

    @Schema(description = "菜单名称")
    private String name;

    @Schema(description="菜单类型（1-菜单 2-目录 3-外链 4-按钮）")
    private Integer type;

    @Schema(description = "路由名称")
    private String routeName;

    @Schema(description = "路由路径")
    private String routePath;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "菜单排序(数字越小排名越靠前)")
    private Integer sort;

    @Schema(description = "菜单是否可见(1:显示;0:隐藏)")
    private Boolean visible;

    @Schema(description="按钮权限标识")
    private String perm;

    @Schema(description = "子菜单")
    private List<MenuVO> children;

}
