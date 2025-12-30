package cloud.ciky.file.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * <p>
 * 文件视图对象
 * </p>
 *
 * @author ciky
 * @since 2025/12/10 11:19
 */
@Schema(description = "文件视图对象")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FileVO {

    @Schema(description = "文件id")
    private String id;

    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "文件URL")
    private String url;

    @Schema(description = "文件桶")
    private String bucket;

    @Schema(description = "文件路径")
    private String path;

}
