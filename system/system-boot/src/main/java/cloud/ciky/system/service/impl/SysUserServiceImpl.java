package cloud.ciky.system.service.impl;

import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.security.service.PermissionService;
import cloud.ciky.security.util.SecurityUtils;
import cloud.ciky.system.model.dto.UserAuthDTO;
import cloud.ciky.system.model.entity.SysUser;
import cloud.ciky.system.mapper.SysUserMapper;
import cloud.ciky.system.model.vo.UserInfoVO;
import cloud.ciky.system.service.SysRoleMenuService;
import cloud.ciky.system.service.SysUserService;
import cn.hutool.core.annotation.scanner.FieldAnnotationScanner;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 16:10:58
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final PermissionService permissionService;
    private final SysRoleMenuService roleMenuService;

    @Override
    public UserAuthDTO getUserAuthInfo(String authKey) {
        UserAuthDTO userAuthDto = this.baseMapper.getUserAuthInfo(authKey);
        return userAuthDto;
    }

    @Override
    public String test() {
        UserAuthDTO user = this.baseMapper.test("ciky");
        return user.getRealName();
    }

    @Override
    public UserInfoVO getCurrentUserInfo() {
        // 登录用户entity
        String userId = SecurityUtils.getUserId();
        UserInfoVO userInfoVO = baseMapper.getUserInfo(userId);
        if (userInfoVO == null) {
            throw new BusinessException("用户不存在");
        }

        // 获取用户角色集合
        Set<String> roles = SecurityUtils.getRoles();
        userInfoVO.setRoles(roles);

        // 获取用户权限集合
        Set<String> perms = new HashSet<>();
        if (CollUtil.isNotEmpty(roles)) {
            Set<String> rolePermsFormCache = permissionService.getRolePermsFormCache(roles);

            // 缓存查询不到, 从数据库查询
            if (CollUtil.isEmpty(rolePermsFormCache)) {
                rolePermsFormCache = roleMenuService.listPermByRoleIds(roles);
            }
            log.info("角色权限列表：{}", rolePermsFormCache);
            perms.addAll(rolePermsFormCache);
        }

        userInfoVO.setPerms(perms);

        return userInfoVO;
    }
}
