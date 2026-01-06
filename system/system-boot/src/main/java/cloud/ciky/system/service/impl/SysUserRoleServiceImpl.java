package cloud.ciky.system.service.impl;

import cloud.ciky.system.model.entity.SysUserRole;
import cloud.ciky.system.mapper.SysUserRoleMapper;
import cloud.ciky.system.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 用户和角色关联表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2025-12-16 15:56:01
 */
@Slf4j
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    public List<String> findRoleCodesByUserId(String userId) {
        return this.baseMapper.findRoleCodesByUserId(userId);
    }

    @Override
    public boolean hasAssignedUsers(String roleId) {
        return this.baseMapper.countUsersForRole(roleId) > 0;
    }
}
