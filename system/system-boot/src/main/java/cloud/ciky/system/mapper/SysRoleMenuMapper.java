package cloud.ciky.system.mapper;

import cloud.ciky.system.model.entity.SysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 角色和菜单关联表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-01-04 18:12:09
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    /**
     * <p>
     * 获取角色的菜单ID集合
     * </p>
     *
     * @author ciky
     * @since 2026/1/6 14:26
     * @param roleId 角色id
     * @return java.util.List<java.lang.String>
     */
    List<String> selectMenuIdsByRoleId(String roleId);
}
