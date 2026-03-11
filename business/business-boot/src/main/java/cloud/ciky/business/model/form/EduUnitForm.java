package cloud.ciky.business.model.form;

import io.seata.core.protocol.MergedWarpMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 * 学院/专业/班级表单对象
 * </p>
 *
 * @author ciky
 * @since 2026-02-05 19:05
 */
@Data
@Schema(description = "学院/专业/班级表单对象")
public class EduUnitForm {

    @Schema(description = "id")
    private String id;

    @Schema(description = "父ID")
    @NotBlank(message = "请选择上级")
    private String parentId;

    @Schema(description = "类型(1-学院 2-专业 3-班级)")
    @NotNull(message = "请选择上级")
    private Integer eduType;

    @Schema(description = "学院/专业/班级名称")
    @NotBlank(message = "名称不能为空")
    private String name;

    @Schema(description = "年级")
    private Integer gradeYear;

    @Schema(description = "班主任Id")
    private String headTeacherId;

    @Schema(description = "班主任名称")
    private String headTeacher;

}
