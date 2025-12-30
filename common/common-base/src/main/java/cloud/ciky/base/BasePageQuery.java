package cloud.ciky.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 基础分页请求对象
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
@Schema(description = "基础分页请求对象")
@Data
public class BasePageQuery implements Serializable {

    @Schema(description = "页码", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pageNum = 1;

    @Schema(description = "每页记录数", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pageSize = 10;
}
