package cloud.ciky.system.model.query;

import cloud.ciky.base.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色分页查询对象
 * </p>
 *
 * @author ciky
 * @since 2026-01-04 17:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色分页查询对象")
public class RolePageQuery extends BaseQuery {

    @Schema(description = "是否为管理员")
    private Boolean isAdmin;
}
