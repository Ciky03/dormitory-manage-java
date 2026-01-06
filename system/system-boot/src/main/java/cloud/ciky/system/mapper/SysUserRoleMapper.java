package cloud.ciky.system.mapper;

import cloud.ciky.system.model.entity.SysUserRole;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 用户和角色关联表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2025-12-16 15:56:01
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    @InterceptorIgnore(dataPermission = "true")
    List<String> findRoleCodesByUserId(String userId);

    /**
     * <p>
     * 获取角色绑定的用户数
     * </p>
     *
     * @author ciky
     * @since 2026/1/4 18:16
     * @param roleId 角色id
     * @return boolean
     */
    int countUsersForRole(String roleId);
}
