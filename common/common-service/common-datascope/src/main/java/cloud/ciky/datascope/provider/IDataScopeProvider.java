package cloud.ciky.datascope.provider;

import cloud.ciky.base.model.DataScope;

/**
 * <p>
 * 数据权限提供器 接口
 * </p>
 *
 * @author ciky
 * @since 2025-12-16 16:30
 */
public interface IDataScopeProvider {
    DataScope getCurrentUserDataScope();
}
