package cloud.ciky.business.model.vo;

import cloud.ciky.base.constant.DateFormatConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 宿舍公约版本视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-25 00:31
 */
@Data
@Schema(description = "宿舍公约版本视图对象")
public class DmConventionVO {

    @Schema(description = "id")
    private String id;

    @Schema(description = "宿舍id")
    private String roomId;

    @Schema(description = "宿舍名称")
    private String roomName;

    @Schema(description = "版本号")
    private Integer versionNo;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "Markdown内容")
    private String contentMd;

    @Schema(description = "状态:0-草稿 1-已发布 2-已作废")
    private Integer status;

    @Schema(description = "是否当前版本")
    private Boolean isCurrent;

    @Schema(description = "发布人")
    private String publishBy;

    @Schema(description = "发布时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime publishTime;

    @Schema(description = "修改人")
    private String updateBy;

    @Schema(description = "修改时间")
    @JsonFormat(pattern = DateFormatConstants.FORMAT)
    private LocalDateTime updateTime;

}

