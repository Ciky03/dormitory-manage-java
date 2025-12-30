package cloud.ciky.base.model;

import cloud.ciky.base.enums.DataScopeEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 数据权限
 * </p>
 *
 * @author ciky
 * @since 2025/12/16 14:22
 */
@Data
public class DataScope {
    private DataScopeEnum type = DataScopeEnum.NONE;

    private List<String> buildingIds = new ArrayList<>();
    private List<String> roomIds = new ArrayList<>();
    private List<String> studentIds = new ArrayList<>();

    // 是否允许公共内容（公告/社区这类表会用到）
    private boolean includePublic = true;
}
