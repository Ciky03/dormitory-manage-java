package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.houbb.sensitive.word.support.ignore.AbstractSensitiveWordCharIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 宿舍分页视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-11 17:57
 */
@Data
@Schema(description = "宿舍分页视图对象")
public class RoomPageVO {

    @Schema(description = "id")
    private String id;

    @Schema(description = "宿舍号")
    private String roomNum;

    @Schema(description = "楼栋号")
    private String buildingNum;

    @Schema(description = "宿舍可住人数")
    private Integer capacity;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime createTime;

}
