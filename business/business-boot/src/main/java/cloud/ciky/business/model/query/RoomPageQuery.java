package cloud.ciky.business.model.query;

import cloud.ciky.base.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 宿舍分页查询对象
 * </p>
 *
 * @author ciky
 * @since 2026/3/11 18:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "宿舍分页查询对象")
public class RoomPageQuery extends BaseQuery {

    @Schema(description = "楼栋id")
    private String parentId;
}
