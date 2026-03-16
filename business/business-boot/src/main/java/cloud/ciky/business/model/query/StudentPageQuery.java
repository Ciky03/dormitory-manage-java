package cloud.ciky.business.model.query;

import cloud.ciky.base.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 学生分页查询对象
 * </p>
 *
 * @author ciky
 * @since 2026-03-16 16:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "学生分页查询对象")
public class StudentPageQuery extends BaseQuery {

}
