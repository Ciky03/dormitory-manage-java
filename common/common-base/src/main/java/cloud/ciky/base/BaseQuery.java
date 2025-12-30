package cloud.ciky.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 基础查询对象
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "基础查询对象")
@Data
public class BaseQuery extends BasePageQuery {

    @Schema(description = "关键字")
    private String keywords;

    @Schema(description = "状态(1->正常；0->禁用)")
    private Boolean status;

    @Schema(description = "状态 1-xxx；2-xxx ...")
    private Integer state;

    @Schema(description = "起始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;

    @Schema(description = "项目ID")
    private String proId;

    @Schema(description = "用户id")
    private String userId;

}
