package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * <p>
 * 教师分页视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-16 16:21
 */
@Data
@Schema(description = "教师分页视图对象")
public class TeacherPageVO {

    @Schema(description = "id")
    private String id;

    @Schema(description = "姓名")
    private String realName;

    @Schema(description = "工号")
    private String teacherNum;

    @Schema(description = "入职日期")
    @JsonFormat(pattern = DateFormatConstants.FORMAT8)
    private LocalDate entryDate;
}
