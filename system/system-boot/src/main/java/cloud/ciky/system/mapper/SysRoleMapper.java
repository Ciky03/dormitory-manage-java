package cloud.ciky.system.mapper;

import cloud.ciky.system.model.entity.SysRole;
import cloud.ciky.system.model.query.RolePageQuery;
import cloud.ciky.system.model.vo.RolePageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2025-12-16 15:56:01
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * <p>
     * 查询角色列表
     * </p>
     *
     * @author ciky
     * @since 2026/1/4 17:39
     * @param objectPage 分页信息
     * @param query 查询条件
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.system.model.vo.RolePageVO>
     */
    Page<RolePageVO> selectRoleList(Page<Object> objectPage, RolePageQuery query);

    @Select("select ifnull(max(sort), 0) from sys_role where delflag = 0 and  status = 1")
    Long selectMaxSort();
}
