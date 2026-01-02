package cloud.ciky.system.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 菜单路由视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-01-02 11:44
 */
@Data
@Schema(description = "菜单路由视图对象")
public class RouteVO {

    @Schema(description = "菜单id")
    private String id;

    @Schema(description = "父菜单id")
    private String parentId;

    @Schema(description = "路由title")
    private String name;

    @Schema(description = "路由名称")
    private String routeName;

    @Schema(description = "路由路径", example = "user")
    private String routePath;

    @Schema(description = "组件路径", example = "system/user/index")
    private String component;

    @Schema(description = "未选中菜单图标")
    private String icon;

    @Schema(description = "选中菜单图标")
    private String pitchIcon;

    @Schema(description = "是否隐藏(true-是 false-否)", example = "true")
    private Boolean visible;

    @Schema(description = "【菜单】是否开启页面缓存", example = "true")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean keepAlive;

    @Schema(description = "子路由列表")
    private List<RouteVO> children;
}
