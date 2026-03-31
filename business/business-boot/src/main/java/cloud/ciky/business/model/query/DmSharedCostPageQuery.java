package cloud.ciky.business.model.query;

import cloud.ciky.base.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 宿舍费用公摊分页查询对象
 *
 * @author ciky
 * @since 2026-03-31 14:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "宿舍费用公摊分页查询对象")
public class DmSharedCostPageQuery extends BaseQuery {

    @Schema(description = "月份，格式yyyy-MM")
    private String month;

    @Schema(description = "是否只看我参与的")
    private Boolean onlyMine;

    @Schema(description = "学生id")
    private String studentId;

    @Schema(description = "宿舍id")
    private String roomId;
}
