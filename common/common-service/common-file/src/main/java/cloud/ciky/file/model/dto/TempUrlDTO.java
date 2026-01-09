package cloud.ciky.file.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 * 文件临时URL信息对象
 * </p>
 *
 * @author ciky
 * @since 2026-01-09 15:07
 */
@Data
@Schema(description = "文件临时URL信息对象")
@AllArgsConstructor
public class TempUrlDTO {

    @Schema(description = "存储桶名称")
    private String bucket;

    @Schema(description = "存储路径")
    private String path;
}
