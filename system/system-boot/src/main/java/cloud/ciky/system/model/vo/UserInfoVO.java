package cloud.ciky.system.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

/**
 * <p>
 * 当前登录用户视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-01-06 16:25
 */
@Data
@Schema(description = "当前登录用户视图对象")
public class UserInfoVO {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "用户姓名")
    private String realName;

    @JsonIgnore
    @Schema(description = "头像附件id")
    private String avatarAttachId;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "性别(1-男 2-女 0-未知)")
    private Integer gender;

    @Schema(description = "用户角色编码集合")
    private Set<String> roles;

    @Schema(description = "用户权限标识集合")
    private Set<String> perms;
}
