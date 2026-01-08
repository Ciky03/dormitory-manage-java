package cloud.ciky.system.service.impl;

import cloud.ciky.security.util.SecurityUtils;
import cloud.ciky.system.model.entity.SysUserRole;
import cloud.ciky.system.mapper.SysUserRoleMapper;
import cloud.ciky.system.service.SysUserRoleService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
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

    @Override
    public boolean saveUserRoles(String userId, List<String> roleIds) {
        String optUser = SecurityUtils.getUserId();
        if (userId == null || CollUtil.isEmpty(roleIds)) {
            // 清空角色
            if(CharSequenceUtil.isNotBlank(userId)){
                this.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
            }
            return true;
        }

        // 用户原角色ID集合
        List<String> userRoleIds = this.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId))
                .stream()
                .map(SysUserRole::getRoleId)
                .toList();

        // 新增用户角色
        List<String> saveRoleIds;
        if (CollUtil.isEmpty(userRoleIds)) {
            saveRoleIds = roleIds;
        } else {
            saveRoleIds = roleIds.stream()
                    .filter(roleId -> !userRoleIds.contains(roleId))
                    .toList();
        }

        List<SysUserRole> userRoles = saveRoleIds.stream().map(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            userRole.setCreateBy(optUser);
            userRole.setCreateTime(LocalDateTime.now());
            return userRole;
        }).toList();

        this.saveBatch(userRoles);

        // 删除用户角色
        if (CollUtil.isNotEmpty(userRoleIds)) {
            List<String> removeRoleIds = userRoleIds.stream()
                    .filter(roleId -> !roleIds.contains(roleId))
                    .toList();

            if (CollUtil.isNotEmpty(removeRoleIds)) {
                this.remove(new LambdaQueryWrapper<SysUserRole>()
                        .eq(SysUserRole::getUserId, userId)
                        .in(SysUserRole::getRoleId, removeRoleIds)
                );
            }
        }
        return true;
    }
}
