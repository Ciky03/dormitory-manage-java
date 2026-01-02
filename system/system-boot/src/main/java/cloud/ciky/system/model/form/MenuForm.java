package cloud.ciky.system.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;


/**
 * <p>
 * 菜单表单对象
 * </p>
 *
 * @author ciky
 * @since 2026-01-02 11:49
 */
@Data
@Schema(description = "菜单表单对象")
public class MenuForm {
    @Schema(description = "菜单ID")
    private String id;

    @Schema(description = "父菜单ID")
    @NotBlank(message = "父菜单ID不能为空")
    private String parentId;

    @Schema(description = "菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @Schema(description = "菜单类型（1-菜单 2-目录 3-外链 4-按钮）")
    @Range(max = 4, min = 1, message = "菜单类型不正确")
    @NotNull(message = "菜单类型不能为空")
    private Integer type;

    @Schema(description = "路由名称")
    private String routeName;

    @Schema(description = "路由路径")
    private String routePath;

    @Schema(description = "组件路径(vue页面完整路径，省略.vue后缀)")
    private String component;

    @Schema(description = "权限标识")
    private String perm;

    @Schema(description = "显示状态(1:显示;0:隐藏)")
    private Boolean visible;

    @Schema(description = "排序(数字越小排名越靠前)")
    @NotNull(message = "排序不能为空")
    @Range(max = 999, min = 0, message = "排序不正确")
    private Integer sort;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "菜单图标")
    private String pitchIcon;

    @Schema(description = "【菜单】是否开启页面缓存", example = "1")
    private Boolean keepAlive;
}
