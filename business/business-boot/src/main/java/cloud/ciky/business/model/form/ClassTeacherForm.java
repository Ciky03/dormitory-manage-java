package cloud.ciky.business.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>
 * 班级教师表单对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-17 18:28
 */
@Data
@Schema(description = "班级教师表单对象")
public class ClassTeacherForm {

    @Schema(description = "教师id")
    @NotBlank(message = "教师id不能为空")
    private String teacherId;

    @Schema(description = "班级id")
    @NotBlank(message = "班级id不能为空")
    private String classId;
}
