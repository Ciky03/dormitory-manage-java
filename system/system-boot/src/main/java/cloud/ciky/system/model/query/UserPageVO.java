package cloud.ciky.system.model.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 用户分页视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-01-07 14:33
 */
@Data
@Schema(description = "用户分页视图对象")
public class UserPageVO {

    @Schema(description="用户ID")
    private String id;

    @Schema(description="用户名")
    private String username;

    @Schema(description="真实姓名")
    private String realName;

    @Schema(description="手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description="用户状态(1:启用;0:禁用)")
    private Boolean status;

    @Schema(description = "性别((1-男 2-女 0-保密)")
    private Integer gender;

    @Schema(description="角色名称，多个使用英文逗号(,)分割")
    private String roleNames;

    @Schema(description="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

}
