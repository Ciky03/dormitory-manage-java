package cloud.ciky.business.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 学院/专业/班级查询对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-17 11:42
 */
@Data
@Schema(description = "学院/专业/班级查询对象")
public class UnitQuery {

    @Schema(description = "是否查询所有(1-学院/专业/班级, 0-学院/专业)")
    private Boolean queryAll;

    @Schema(description = "学生id")
    private String studentId;

    @Schema(description = "教师id")
    private String teacherId;
}
