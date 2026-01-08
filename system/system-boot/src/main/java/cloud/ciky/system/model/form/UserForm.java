package cloud.ciky.system.model.form;

import cloud.ciky.base.constant.SystemConstants;
import cloud.ciky.base.model.ValidationGroups;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.format.DecimalStyle;
import java.util.List;

/**
 * <p>
 * 用户表单对象
 * </p>
 *
 * @author ciky
 * @since 2026-01-07 14:35
 */
@Data
@Schema(description = "用户表单对象")
public class UserForm {

    @Schema(description = "用户id")
    private String id;

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "密码")
    @Pattern(regexp = SystemConstants.PASSWORD_REGEX, message = "密码至少包含大小写字母和数字，长度至少为8位")
    private String password;

    @Schema(description = "确认密码")
    @Pattern(regexp = SystemConstants.PASSWORD_REGEX, message = "密码至少包含大小写字母和数字，长度至少为8位")
    private String confirmPassword;

    @Schema(description = "手机号码")
    @NotBlank(message = "手机号码不能为空")
    private String phone;

    @Schema(description = "邮箱号码")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Schema(description = "角色ID集合")
    private List<String> roleIds;
}
