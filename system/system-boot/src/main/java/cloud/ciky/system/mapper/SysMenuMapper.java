package cloud.ciky.system.mapper;

import cloud.ciky.system.model.entity.SysMenu;
import cloud.ciky.system.model.vo.RouteVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-01-01 17:27:49
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    @Select("select ifnull(max(sort), 0) from sys_menu where delflag = 0 and visible = 1 and parent_id = #{parentId}")
    Long selectMaxSort(String parentId);

    /**
     * <p>
     * 获取菜单路由列表
     * </p>
     *
     * @author ciky
     * @since 2026/1/2 23:27
     * @param roles 角色Code
     * @return java.util.List<cloud.ciky.system.model.vo.RouteVO>
     */
    List<RouteVO> listRoutes(Set<String> roles);
}
