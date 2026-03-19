package cloud.ciky.business.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 学生分页视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-16 16:21
 */
@Data
@Schema(description = "学生分页视图对象")
public class StudentPageVO {

    @Schema(description = "id")
    private String id;

    @Schema(description = "姓名")
    private String realName;

    @Schema(description = "学号")
    private String studentNum;

    @Schema(description = "班级")
    private String className;

    @Schema(description = "宿舍")
    private String roomName;

    @Schema(description = "入学年份")
    private Integer admissionYear;

    @Schema(description = "毕业年份")
    private Integer graduationYear;
}
