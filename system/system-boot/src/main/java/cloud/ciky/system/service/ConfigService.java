package cloud.ciky.system.service;

/**
 * <p>
 * 系统配置 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-01-01 17:27:49
 */
public interface ConfigService {
    /**
     * <p>
     * 获取排序
     * </p>
     *
     * @author ciky
     * @since 2026/1/2 18:45
     * @param business 业务
     * @param parentId 父id
     * @return java.lang.Long
     */
    Long getSort(String business, String parentId);
}
