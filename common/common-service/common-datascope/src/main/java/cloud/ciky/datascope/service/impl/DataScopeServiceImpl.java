package cloud.ciky.datascope.service.impl;

import cloud.ciky.datascope.mapper.DataScopeMapper;
import cloud.ciky.datascope.service.DataScopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 数据权限 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2025-12-17 11:51
 */
@Service
@RequiredArgsConstructor
public class DataScopeServiceImpl implements DataScopeService{

    private final DataScopeMapper dataScopeMapper;

    public List<String> findRoleCodesByUserId(String userId) {
       return dataScopeMapper.findRoleCodesByUserId(userId);
    }

}
