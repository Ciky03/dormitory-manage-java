package cloud.ciky.business.model.query;

import cloud.ciky.base.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Todo page query.
 *
 * @author ciky
 * @since 2026-03-26 17:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Dorm todo page query")
public class DmTodoPageQuery extends BaseQuery {

    @Schema(description = "Priority")
    private Integer priority;

    @Schema(description = "Assignee student id")
    private String assigneeStudentId;

    @Schema(description = "Due type")
    private Integer dueType;
}
