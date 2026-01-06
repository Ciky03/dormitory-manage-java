package cloud.ciky.system.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * <p>
 * 角色权限业务对象
 * </p>
 *
 * @author ciky
 * @since 2026-01-06 15:54
 */
@Data
@Schema(description = "角色权限业务对象")
public class RolePermsBO {
    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 权限标识集合
     */
    private Set<String> perms;
}
