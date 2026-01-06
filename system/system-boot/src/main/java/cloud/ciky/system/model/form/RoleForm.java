package cloud.ciky.system.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 * 角色表单对象
 * </p>
 *
 * @author ciky
 * @since 2026-01-04 17:30
 */
@Data
@Schema(description = "角色表单对象")
public class RoleForm {

    @Schema(description="角色ID")
    private String id;

    @Schema(description="角色名称")
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @Schema(description="角色编码")
    @NotBlank(message = "角色编码不能为空")
    private String code;

    @Schema(description="显示顺序（数字越小排名越靠前）")
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    @Schema(description="角色描述")
    private String remark;

    @Schema(description="数据权限(0-所有数据 1-楼栋 2-宿舍 3-学生 4-无权限)")
    private Integer dataScope;

}
