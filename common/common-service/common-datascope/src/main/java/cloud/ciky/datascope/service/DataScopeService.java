package cloud.ciky.datascope.service;


import java.util.List;

/**
 * <p>
 * 数据权限 服务类
 * </p>
 *
 * @author ciky
 * @since 2025-12-17 11:49
 */
public interface DataScopeService {

    /**
     * <p>
     * 查询用户角色code
     * </p>
     *
     * @author ciky
     * @since 2025/12/16 16:41
     * @param userId 用户id
     * @return java.util.List<java.lang.String>
     */
    List<String> findRoleCodesByUserId(String userId);
}
