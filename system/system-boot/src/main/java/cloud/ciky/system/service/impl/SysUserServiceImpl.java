package cloud.ciky.system.service.impl;

import cloud.ciky.security.util.SecurityUtils;
import cloud.ciky.system.model.dto.UserAuthDTO;
import cloud.ciky.system.model.entity.SysUser;
import cloud.ciky.system.mapper.SysUserMapper;
import cloud.ciky.system.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

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
}
