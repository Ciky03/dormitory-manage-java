package cloud.ciky.business.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>
 * 学生宿舍表单对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-19 17:33
 */
@Data
@Schema(description = "学生宿舍表单对象")
public class RoomStudentForm {

    @Schema(description = "学生id")
    @NotBlank(message = "学生id不能为空")
    private String studentId;

    @Schema(description = "宿舍id")
    @NotBlank(message = "宿舍id不能为空")
    private String roomId;
}
