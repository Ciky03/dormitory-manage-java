package cloud.ciky.business.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 学院/专业/班级视图对象
 * </p>
 *
 * @author ciky
 * @since 2026-02-05 17:24
 */
@Data
@Schema(description = "学院/专业/班级视图对象")
public class EduUnitVO {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "父ID")
    private String parentId;

    @Schema(description = "父路径")
    private String treePath;

    @Schema(description = "名称")
    private String name;

    @Schema(description="类型（1-学院 2-专业 3-班级）")
    private Integer type;

    @Schema(description = "是否已选择(1-已选择 2-未选择)")
    private Boolean selected;

    @Schema(description = "子菜单")
    private List<EduUnitVO> children;
}
