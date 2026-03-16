package cloud.ciky.system.service.impl;

import cloud.ciky.base.BaseQuery;
import cloud.ciky.base.IBaseEnum;
import cloud.ciky.base.constant.RedisConstants;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.base.model.Option;
import cloud.ciky.file.model.dto.FileDTO;
import cloud.ciky.file.model.dto.TempUrlDTO;
import cloud.ciky.file.service.OssService;
import cloud.ciky.file.service.impl.MinioOssService;
import cloud.ciky.mail.service.MailService;
import cloud.ciky.security.service.PermissionService;
import cloud.ciky.security.util.SecurityUtils;
import cloud.ciky.system.enums.UserTypeEnum;
import cloud.ciky.system.model.dto.UserAuthDTO;
import cloud.ciky.system.model.entity.SysAttach;
import cloud.ciky.system.model.entity.SysUser;
import cloud.ciky.system.mapper.SysUserMapper;
import cloud.ciky.system.model.entity.SysUserRole;
import cloud.ciky.system.model.form.PwdUpdateForm;
import cloud.ciky.system.model.form.UserForm;
import cloud.ciky.system.model.query.UserPageVO;
import cloud.ciky.system.model.vo.UserInfoVO;
import cloud.ciky.system.service.*;
import cloud.ciky.system.util.PasswordGenerator;
import cn.hutool.core.annotation.scanner.FieldAnnotationScanner;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
    private final SysRoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final SysUserRoleService userRoleService;
    private final MailService mailService;
    private final SysAttachService attachService;
    private final OssService ossService;
    private final StringRedisTemplate redisTemplate;

    @Override
    public UserAuthDTO getUserAuthInfo(String authKey, String wxMpOpenId) {
        UserAuthDTO userAuthDto = this.baseMapper.getUserAuthInfo(authKey, wxMpOpenId);
        return userAuthDto;
    }

    @Override
    public UserInfoVO getCurrentUserInfo() {
        // 登录用户entity
        String userId = SecurityUtils.getUserId();
        UserInfoVO userInfoVO = baseMapper.getUserInfo(userId);
        if (userInfoVO == null) {
            throw new BusinessException("用户不存在");
        }
        // 是否绑定微信公众号
        userInfoVO.setIsBindWxMp(CharSequenceUtil.isNotBlank(userInfoVO.getWxMpOpenId()));

        // 获取附件信息
        String avatarAttachId = userInfoVO.getAvatarAttachId();
        userInfoVO.setAvatar(getAvatarAttachUrl(avatarAttachId));

        // 获取用户角色集合
        Set<String> roles = SecurityUtils.getRoles();
        userInfoVO.setRoles(roles);

        // 获取用户权限集合
        userInfoVO.setPerms(getUserPerms(roles));

        return userInfoVO;
    }

    /**
     * <p>
     * 获取用户权限集合
     * </p>
     *
     * @param roles 角色编码集合
     * @return java.util.Set<java.lang.String>
     * @author ciky
     * @since 2026/1/9 15:23
     */
    @NotNull
    private Set<String> getUserPerms(Set<String> roles) {
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
        return perms;
    }

    /**
     * <p>
     * 获取头像附件临时URL
     * </p>
     *
     * @param avatarAttachId 头像附件id
     * @return java.lang.String
     * @author ciky
     * @since 2026/1/9 15:23
     */
    private String getAvatarAttachUrl(String avatarAttachId) {
        if (CharSequenceUtil.isNotBlank(avatarAttachId)) {
            // 从redis获取
            String avatar = redisTemplate.opsForValue().get(RedisConstants.Attach.AVATAR + avatarAttachId);
            if (CharSequenceUtil.isBlank(avatar)) {
                // 缓存查询不到,获取附件临时url
                SysAttach avatarAttach = attachService.getById(avatarAttachId);
                if (avatarAttach == null) {
                    return null;
                }
                TempUrlDTO dto = new TempUrlDTO(avatarAttach.getBucket(), avatarAttach.getPath());
                avatar = ossService.getTempUrl(dto);
                log.info("用户头像url:{}", avatar);
                // 缓存到redis
                redisTemplate.opsForValue().set(RedisConstants.Attach.AVATAR + avatarAttachId, avatar, 3, TimeUnit.DAYS);
            }
            return avatar;
        }
        return null;
    }

    @Override
    public Page<UserPageVO> getUserListPage(BaseQuery query) {
        return this.baseMapper.selectUserListPage(new Page<>(query.getPageNum(), query.getPageSize()), query);
    }

    @Override
    public List<Option<String>> listUserOptions() {
        return null;
    }

    @Override
    public UserForm getUserForm(String userId) {
        UserForm user = this.baseMapper.getUserForm(userId);
        if (user == null) {
            throw new BusinessException("用户不存在!");
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUser(UserForm formData) {
        String id = formData.getId();
        String username = formData.getUsername();
        String phone = formData.getPhone();
        String email = formData.getEmail();
        String optUser = SecurityUtils.getUserId();
        String password = formData.getPassword();
        String realName = formData.getRealName();
        Integer userType = formData.getUserType();
        boolean userIdExisted = CharSequenceUtil.isNotBlank(id);

        // 判断密码
        if (CharSequenceUtil.isNotBlank(password)) {
            if (!password.equals(formData.getConfirmPassword())) {
                throw new BusinessException("密码不一致，请重新输入！");
            }
        }

        // 判断用户名
        boolean usernameExisted = this.exists(new LambdaQueryWrapper<SysUser>()
                .ne(userIdExisted, SysUser::getId, id).eq(SysUser::getUsername, username));
        if (usernameExisted) {
            throw new BusinessException("用户名已存在，请修改后重试！");
        }

        // 判断手机号
        if (!Validator.isMobile(phone)) {
            throw new BusinessException("手机号码不正确!");
        }
        boolean phoneExisted = this.exists(new LambdaQueryWrapper<SysUser>()
                .ne(userIdExisted, SysUser::getId, id).eq(SysUser::getPhone, phone));
        if (phoneExisted) {
            throw new BusinessException("手机号已存在，请修改后重试！");
        }

        // 判断邮箱
        if (CharSequenceUtil.isNotBlank(email)) {
            if (!Validator.isEmail(email)) {
                throw new BusinessException("邮箱格式不正确!");
            }
            boolean emailExisted = this.exists(new LambdaQueryWrapper<SysUser>()
                    .ne(userIdExisted, SysUser::getId, id).eq(SysUser::getEmail, email));
            if (emailExisted) {
                throw new BusinessException("邮箱已存在，请修改后重试！");
            }
        }

        SysUser entity = new SysUser();
        if (userIdExisted) {
            entity.setId(id);
            entity.setUpdateBy(optUser);
        } else {
            entity.setCreateBy(optUser);
        }

        if (CharSequenceUtil.isNotBlank(password)) {
            entity.setPassword(passwordEncoder.encode(password));
        }
        if (CharSequenceUtil.isNotBlank(realName)) {
            entity.setNickname(PinyinUtil.getPinyin(realName, ""));
            entity.setRealName(realName);
        }
        entity.setUsername(formData.getUsername());
        entity.setPhone(formData.getPhone());
        entity.setEmail(formData.getEmail());
        entity.setUserType(userType);
        entity.setBusinessUserId(formData.getBusinessUserId());
        entity.setStatus(true);
        boolean result = this.saveOrUpdate(entity);

        // 学生/教师/宿管
        if (UserTypeEnum.STUDENT.getValue().equals(userType)
                || UserTypeEnum.TEACHER.getValue().equals(userType)
                || UserTypeEnum.DORMITORY_MANAGER.getValue().equals(userType)) {
            String roleCode = IBaseEnum.getEnumByValue(userType, UserTypeEnum.class).getCode();
            formData.setRoleIds(Collections.singletonList(roleService.getRoleIdByCode(roleCode)));
        }

        // 保存用户角色
        if (result) {
            result = userRoleService.saveUserRoles(entity.getId(), formData.getRoleIds());
        }
        return result;
    }

    @Override
    public boolean updateUserStatus(String userId, Boolean status) {
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (user.getStatus().equals(status)) {
            throw new BusinessException("用户状态未发生变化");
        }

        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<SysUser>()
                .set(SysUser::getStatus, status)
                .set(SysUser::getUpdateBy, SecurityUtils.getUserId())
                .eq(SysUser::getId, userId);

        return this.update(wrapper);
    }

    @Override
    public boolean deleteUsers(String ids) {
        if (CharSequenceUtil.isBlank(ids)) {
            return false;
        }
        List<String> userIds = Arrays.stream(ids.split(",")).toList();
        for (String userId : userIds) {
            SysUser user = this.getById(userId);
            if (user == null) {
                continue;
            }
            // 删除用户
            boolean removed = this.removeById(userId);

            // 删除用户角色
            if (removed) {
                userRoleService.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String resetPassword(String userId) {
        SysUser entity = this.getById(userId);
        if (entity == null) {
            throw new BusinessException("用户不存在");
        }
        String email = entity.getEmail();
        String newPwd = PasswordGenerator.generatePassword();
        String realName = entity.getRealName();
        if (CharSequenceUtil.isNotBlank(email)) {
            if (Validator.isEmail(email)) {
                // 创建模板配置
                TemplateConfig config = new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH);
                // 获取模板引擎
                TemplateEngine engine = TemplateUtil.createEngine(config);
                // 获取模板
                Template template = engine.getTemplate("resetPwd.ftl");
                // 准备数据
                Map<String, Object> data = new HashMap<>();

                data.put("newPwd", newPwd);
                data.put("realName", realName);
                // 渲染模板
                String render = template.render(data);
                mailService.sendHtmlMail(email, "密码重置提醒（重要）", render);
            }
        }
        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<SysUser>()
                .set(SysUser::getPassword, passwordEncoder.encode(newPwd))
                .set(SysUser::getUpdateBy, SecurityUtils.getUserId())
                .eq(SysUser::getId, userId);

        this.update(wrapper);
        return newPwd;
    }

    @Override
    public boolean changePassword(String userId, PwdUpdateForm form) {
        String optUser = SecurityUtils.getUserId();
        SysUser user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        String oldPassword = form.getOldPassword();
        String newPassword = form.getNewPassword();
        String confirmPassword = form.getConfirmPassword();

        if (!newPassword.equals(confirmPassword)) {
            throw new BusinessException("密码不一致，请重新输入！");
        }
        // 校验原密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        // 新旧密码不能相同
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new BusinessException("新密码不能与原密码相同");
        }

        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<SysUser>()
                .set(SysUser::getPassword, passwordEncoder.encode(newPassword))
                .set(SysUser::getUpdateBy, optUser)
                .eq(SysUser::getId, userId);

        return this.update(wrapper);
    }

    @Override
    public boolean changeAvatar(String userId, String attachId) {
        String optUser = SecurityUtils.getUserId();

        // 判断附件是否存在
        SysAttach attach = attachService.getById(attachId);
        if (attach == null) {
            throw new BusinessException("头像信息未保存, 请联系管理员!");
        }

        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<SysUser>()
                .set(SysUser::getAvatarAttachId, attachId)
                .set(SysUser::getUpdateBy, optUser)
                .eq(SysUser::getId, userId);

        return this.update(wrapper);
    }

    @Override
    public boolean bindWxMp(String userId, String openId) {
        // 去重
        long count = this.count(new LambdaQueryWrapper<SysUser>()
                .ne(SysUser::getId, userId)
                .eq(SysUser::getWxMpOpenId, openId));
        if (count > 0) {
            throw new BusinessException("该微信已绑定其他用户，请解绑后重试！");
        }

        LambdaUpdateWrapper<SysUser> wrapper = new LambdaUpdateWrapper<SysUser>()
                .set(SysUser::getWxMpOpenId, openId)
                .set(SysUser::getUpdateBy, userId)
                .eq(SysUser::getId, userId);

        return this.update(wrapper);
    }

    @Override
    public boolean unbindBusiness(String businessUserId) {
        String optUser = SecurityUtils.getUserId();
        // 不清空business_user_id, 保留原记录
        return this.update(new LambdaUpdateWrapper<SysUser>()
                .setSql("history_user_type = user_type")
                .set(SysUser::getUserType, UserTypeEnum.OTHER.getValue())
                .set(SysUser::getUpdateBy, optUser)
                .set(SysUser::getUpdateTime, LocalDateTime.now())
                .eq(SysUser::getBusinessUserId, businessUserId));
    }
}
