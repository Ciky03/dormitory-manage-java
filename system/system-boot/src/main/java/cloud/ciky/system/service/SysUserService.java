package cloud.ciky.system.service;

import cloud.ciky.base.BaseQuery;
import cloud.ciky.base.model.Option;
import cloud.ciky.system.model.dto.UserAuthDTO;
import cloud.ciky.system.model.entity.SysUser;
import cloud.ciky.system.model.form.PwdUpdateForm;
import cloud.ciky.system.model.form.UserForm;
import cloud.ciky.system.model.query.UserPageVO;
import cloud.ciky.system.model.vo.UserInfoVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    /**
     * <p>
     * 获取登录用户信息
     * </p>
     *
     * @author ciky
     * @since 2026/1/6 16:34
     * @return cloud.ciky.system.model.vo.UserInfoVO
     */
    UserInfoVO getCurrentUserInfo();

    /**
     * <p>
     * 获取用户分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/1/7 14:43
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.system.model.query.UserPageVO>
     */
    Page<UserPageVO> getUserListPage(BaseQuery query);

    /**
     * <p>
     * 用户下拉选项
     * </p>
     *
     * @author ciky
     * @since 2026/1/7 14:45
     * @return java.util.List<cloud.ciky.base.model.Option<java.lang.String>>
     */
    List<Option<String>> listUserOptions();

    /**
     * <p>
     * 获取用户表单数据
     * </p>
     *
     * @author ciky
     * @since 2026/1/7 14:47
     * @param userId 用户id
     * @return cloud.ciky.system.model.form.UserForm
     */
    UserForm getUserForm(String userId);

    /**
     * <p>
     * 保存用户
     * </p>
     *
     * @author ciky
     * @since 2026/1/7 14:47
     * @param userForm 表单对象
     * @return java.lang.String
     */
    String saveUser(UserForm userForm);

    /**
     * <p>
     * 修改用户状态
     * </p>
     *
     * @author ciky
     * @since 2026/1/7 14:47
     * @param userId 用户id
     * @param status 用户状态(1:启用;0:禁用)
     * @return boolean
     */
    boolean updateUserStatus(String userId, Boolean status);

    /**
     * <p>
     * 删除用户
     * </p>
     *
     * @author ciky
     * @since 2026/1/7 14:48
     * @param ids 用户id
     * @return boolean
     */
    boolean deleteUsers(String ids);

    /**
     * <p>
     * 重置用户密码
     * </p>
     *
     * @author ciky
     * @since 2026/1/7 14:48
     * @param userId 用户id
     * @return java.lang.String
     */
    String resetPassword(String userId);

    /**
     * <p>
     * 修改密码
     * </p>
     *
     * @author ciky
     * @since 2026/1/8 16:56
     * @param userId 用户id
     * @param form 表单对象
     * @return boolean
     */
    boolean changePassword(String userId, PwdUpdateForm form);

    /**
     * <p>
     * 修改头像
     * </p>
     *
     * @author ciky
     * @since 2026/1/9 1:14
     * @param userId 用户id
     * @param attachId 附件id (sys_attach表主键)
     * @return boolean
     */
    boolean changeAvatar(String userId, String attachId);

    /**
     * <p>
     * 绑定微信公众号
     * </p>
     *
     * @author ciky
     * @since 2026/1/10 23:54
     * @param userId 用户id
     * @param openId 微信公众号openId
     * @return boolean
     */
    boolean bindWxMp(String userId, String openId);
}
