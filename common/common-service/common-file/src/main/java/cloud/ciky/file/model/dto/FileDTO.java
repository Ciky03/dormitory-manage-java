package cloud.ciky.file.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 文件信息对象
 * </p>
 *
 * @author ciky
 * @since 2025/12/10 11:20
 */
@Data
@Schema(description = "文件信息对象")
public class FileDTO {

    @Schema(description = "文件ID,文件md5值")
    private String id;

    @Schema(description = "文件md5值")
    private String md5;

    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "文件目录ID")
    private String dirId;

    @Schema(description = "存储桶名称")
    private String bucket;

    @Schema(description = "文件URL")
    private String url;

    @Schema(description = "存储路径")
    private String path;

    @Schema(description = "文件大小")
    private Long size;

    @Schema(description = "文件媒体类型")
    private String mimeType;

    @Schema(description = "是否公开")
    private Boolean isPublic;
}
