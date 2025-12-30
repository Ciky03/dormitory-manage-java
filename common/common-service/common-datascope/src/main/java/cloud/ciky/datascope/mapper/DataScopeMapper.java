package cloud.ciky.datascope.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 数据权限 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 16:10:58
 */
@Mapper
public interface DataScopeMapper {

    /**
     * <p>
     * 查询用户角色code
     * </p>
     *
     * @author ciky
     * @since 2025/12/17 12:03
     * @param userId
     * @return java.util.List<java.lang.String>
     */
    List<String> findRoleCodesByUserId(String userId);
}
