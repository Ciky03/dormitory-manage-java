package cloud.ciky.system.model.entity;

import cloud.ciky.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 16:10:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class SysUser extends BaseEntity {

    /**
     * 用户名(登录,唯一)
     */
    @TableField("username")
    private String username;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 状态(0-禁用 1-正常)
     */
    @TableField("status")
    private Boolean status;

    /**
     * 用户头像
     */
    @TableField("avatar")
    private String avatar;


    /**
     * 微信unionId
     */
    @TableField("union_id")
    private String unionId;

}
