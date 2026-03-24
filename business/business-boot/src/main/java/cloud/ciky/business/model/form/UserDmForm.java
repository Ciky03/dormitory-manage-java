package cloud.ciky.business.model.form;

import cloud.ciky.system.model.form.UserForm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * <p>
 * 宿管表单对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-15 11:32
 */
@Data
@Schema(description = "宿管表单对象")
public class UserDmForm {

    @Schema(description = "宿管id")
    private String id;

    @Schema(description = "姓名")
    @NotBlank(message = "姓名不能为空")
    private String realName;

    @Schema(description = "工号")
    @NotBlank(message = "工号不能为空")
    private String dmNum;

    @Schema(description = "入职日期")
    @NotNull(message = "入职日期不能为空")
    private LocalDate entryDate;

    @Schema(description = "用户表单对象")
    private UserForm userForm;

}
