package cloud.ciky.system.service.impl;

import cloud.ciky.base.constant.RedisConstants;
import cloud.ciky.system.model.bo.RolePermsBO;
import cloud.ciky.system.model.entity.SysRoleMenu;
import cloud.ciky.system.mapper.SysRoleMenuMapper;
import cloud.ciky.system.service.SysRoleMenuService;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-01-04 18:12:09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<String> listMenuIdsByRoleId(String roleId) {
        return this.baseMapper.selectMenuIdsByRoleId(roleId);
    }

    @Override
    public void refreshRolePermsCache() {
        // 清理权限缓存
        redisTemplate.opsForHash().delete(RedisConstants.System.ROLE_PERMS, "*");

        List<RolePermsBO> roleList = this.baseMapper.selectRolePermsList(null);
        if (CollUtil.isNotEmpty(roleList)) {
            roleList.forEach(item -> {
                String roleCode = item.getRoleCode();
                Set<String> perms = item.getPerms();
                redisTemplate.opsForHash().put(RedisConstants.System.ROLE_PERMS, roleCode, perms);
            });
        }
    }

    @Override
    public void refreshRolePermsCache(String roleCode) {
        // 清理权限缓存
        redisTemplate.opsForHash().delete(RedisConstants.System.ROLE_PERMS, roleCode);

        List<RolePermsBO> roleList = this.baseMapper.selectRolePermsList(roleCode);
        if (CollUtil.isNotEmpty(roleList)) {
            RolePermsBO rolePerms = roleList.get(0);
            if (rolePerms == null) {
                return;
            }

            Set<String> perms = rolePerms.getPerms();
            redisTemplate.opsForHash().put(RedisConstants.System.ROLE_PERMS, roleCode, perms);
        }
    }

    @Override
    public void refreshRolePermsCache(String oldRoleCode, String newRoleCode) {
        // 清理旧角色权限缓存
        redisTemplate.opsForHash().delete(RedisConstants.System.ROLE_PERMS, oldRoleCode);

        // 添加新角色权限缓存
        List<RolePermsBO> roleList = this.baseMapper.selectRolePermsList(newRoleCode);
        if (CollUtil.isNotEmpty(roleList)) {
            RolePermsBO rolePerms = roleList.get(0);
            if (rolePerms == null) {
                return;
            }

            Set<String> perms = rolePerms.getPerms();
            redisTemplate.opsForHash().put(RedisConstants.System.ROLE_PERMS, newRoleCode, perms);
        }
    }
}
