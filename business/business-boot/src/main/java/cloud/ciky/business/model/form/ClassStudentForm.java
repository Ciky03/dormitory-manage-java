package cloud.ciky.business.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>
 * 班级学生表单对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-17 18:20
 */
@Data
@Schema(description = "班级学生表单对象")
public class ClassStudentForm {

    @Schema(description = "学生id")
    @NotBlank(message = "学生id不能为空")
    private String studentId;

    @Schema(description = "班级id")
    @NotBlank(message = "班级id不能为空")
    private String classId;
}
