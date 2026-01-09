package cloud.ciky.system.model.dto;

import lombok.Data;

import java.util.Set;

/**
 * <p>
 * 用户认证信息传输层对象
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 17:13
 */
@Data
public class UserAuthDTO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户状态(1:正常;0:禁用)
     */
    private Boolean status;

    /**
     * 用户角色编码集合 ["ROOT","ADMIN"]
     */
    private Set<String> roles;

    /**
     * 用户权限标识集合
     */
    private Set<String> perms;

    /**
     * 数据权限范围
     */
    private Integer dataScope;

    /**
     * 昵称(OIDC UserInfo)
     */
    private String nickname;

    /**
     * 手机号(OIDC UserInfo)
     */
    private String phone;

    /**
     * 邮箱(OIDC UserInfo)
     */
    private String email;

    /**
     * 头像附件id(OIDC UserInfo)
     */
    private String avatarAttachId;

}
