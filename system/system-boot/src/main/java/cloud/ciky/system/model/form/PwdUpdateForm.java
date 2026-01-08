package cloud.ciky.system.model.form;

import cloud.ciky.base.constant.SystemConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * <p>
 * 修改密码表单对象
 * </p>
 *
 * @author ciky
 * @since 2026-01-08 16:53
 */
@Data
@Schema(description = "修改密码表单对象")
public class PwdUpdateForm {

    @Schema(description = "原密码")
    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @Schema(description = "新密码")
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = SystemConstants.PASSWORD_REGEX, message = "密码至少包含大小写字母和数字，长度至少为8位")
    private String newPassword;

    @Schema(description = "确认密码")
    @NotBlank(message = "新密码不能为空")
    @Pattern(regexp = SystemConstants.PASSWORD_REGEX, message = "密码至少包含大小写字母和数字，长度至少为8位")
    private String confirmPassword;


}
