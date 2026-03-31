package cloud.ciky.business.model.query;

import cloud.ciky.base.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 宿舍待办分页查询对象
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "宿舍待办分页查询对象")
public class DmTodoPageQuery extends BaseQuery {

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "负责人学生编号")
    private String assigneeStudentId;

    @Schema(description = "到期类型")
    private Integer dueType;
}
