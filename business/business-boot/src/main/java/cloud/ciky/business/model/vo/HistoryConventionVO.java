package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 历史宿舍公约视图
 * </p>
 *
 * @author ciky
 * @since 2026-03-25 11:45
 */
@Data
@Schema(description = "历史宿舍公约视图")
public class HistoryConventionVO {

    @Schema(description = "id")
    private String id;

    @Schema(description = "版本号")
    private Integer versionNo;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "状态:0-草稿 1-已发布 2-已作废")
    private Integer status;

    @Schema(description = "是否可编辑")
    private Boolean editable;

    @Schema(description = "发布时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime publishTime;

    @Schema(description = "发布人")
    private String publishBy;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime updateTime;

    @Schema(description = "修改人")
    private String updateBy;

    @JsonIgnore
    @Schema(description = "修改人id")
    private String updateUserId;
}
