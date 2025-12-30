package cloud.ciky.base.constant;

/**
 * <p>
 * JWT声明常量
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
public interface JwtClaimConstants {

    /**
     * 用户ID
     */
    String USER_ID = "userId";

    /**
     * 用户名
     */
    String USERNAME = "username";


    /**
     * 真实姓名
     */
    String REAL_NAME = "realname";

    /**
     * 部门ID
     */
    String DEPT_ID = "deptId";

    /**
     * 数据权限
     */
    String DATA_SCOPE = "dataScope";

    /**
     * 权限(角色Code)集合
     */
    String AUTHORITIES = "authorities";

}
