package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

/**
 * <p>
 * 宿管分页视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-16 16:21
 */
@Data
@Schema(description = "宿管分页视图对象")
public class DormitoryManagerPageVO {

    @Schema(description = "id")
    private String id;

    @Schema(description = "姓名")
    private String realName;

    @Schema(description = "工号")
    private String dmNum;

    @Schema(description = "楼栋名")
    private String building;

    @Schema(description = "入职日期")
    @JsonFormat(pattern = DateFormatConstants.FORMAT8)
    private LocalDate entryDate;
}
