package cloud.ciky.system.mapper;

import cloud.ciky.core.annotation.DataPermission;
import cloud.ciky.system.model.dto.UserAuthDTO;
import cloud.ciky.system.model.entity.SysUser;
import cloud.ciky.system.model.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 16:10:58
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * <p>
     * 获取用户认证信息
     * </p>
     *
     * @author ciky
     * @since 2025/12/15 17:33
     * @param authKey 用户名/手机号/邮箱
     * @return cloud.ciky.system.model.dto.UserAuthDTO
     */
//    @DataPermission
    UserAuthDTO getUserAuthInfo(String authKey);

    @DataPermission(mainAlias = "su")
    UserAuthDTO test(String authKey);

    /**
     * <p>
     * 获取用户登录信息
     * </p>
     *
     * @author ciky
     * @since 2026/1/6 16:47
     * @param userId 用户id
     * @return cloud.ciky.system.model.vo.UserInfoVO
     */
    UserInfoVO getUserInfo(String userId);
}
