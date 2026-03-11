package cloud.ciky.business.model.query;

import cloud.ciky.base.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 班级分页查询对象
 * </p>
 *
 * @author ciky
 * @since 2026-02-05 18:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "班级分页查询对象")
public class ClassPageQuery extends BaseQuery {

    @Schema(description = "学院/专业id")
    private String parentId;
}
