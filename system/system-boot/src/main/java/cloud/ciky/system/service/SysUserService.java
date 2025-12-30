package cloud.ciky.system.service;

import cloud.ciky.system.model.dto.UserAuthDTO;
import cloud.ciky.system.model.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 16:10:58
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * <p>
     * 获取用户认证信息
     * </p>
     *
     * @author ciky
     * @since 2025/12/15 17:26
     * @param authKey 用户名/手机号/邮箱
     * @return cloud.ciky.system.model.dto.UserAuthDTO
     */
    UserAuthDTO getUserAuthInfo(String authKey);

    String test();
}
