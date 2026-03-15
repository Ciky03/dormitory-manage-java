package cloud.ciky.business.model.form;

import cloud.ciky.system.model.form.UserForm;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 * 学生表单对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-13 11:19
 */
@Data
@Schema(description = "学生表单对象")
public class UserStudentForm {

    @Schema(description = "学生id")
    private String id;

    @Schema(description = "姓名")
    @NotBlank(message = "姓名不能为空")
    private String realName;

    @Schema(description = "学号")
    @NotBlank(message = "学号不能为空")
    private String studentNum;

    @Schema(description = "入学年份")
    @NotNull(message = "入学年份不能为空")
    private Integer admissionYear;

    @Schema(description = "毕业年份")
    @NotNull(message = "毕业年份不能为空")
    private Integer graduationYear;

    @Schema(description = "用户表单对象")
    private UserForm userForm;

}
