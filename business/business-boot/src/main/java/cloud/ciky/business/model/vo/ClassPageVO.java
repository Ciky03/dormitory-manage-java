package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 班级视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-02-05 17:42
 */
@Data
@Schema(description = "班级视图对象")
public class ClassPageVO {

    @Schema(description = "主键")
    private String id;

    @Schema(description = "班级名称")
    private String className;

    @Schema(description = "学院id")
    private String collegeId;
    @Schema(description = "学院名称")
    private String collegeName;

    @Schema(description = "专业id")
    private String majorId;
    @Schema(description = "专业名称")
    private String majorName;

    @Schema(description = "年级")
    private Integer gradeYear;

    @Schema(description = "班主任id")
    private String headTeacherId;
    @Schema(description = "班主任")
    private String headTeacher;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime createTime;

}
