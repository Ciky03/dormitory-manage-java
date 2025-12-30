package cloud.ciky.system.model.entity;

import cloud.ciky.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * <p>
 * 用户和角色关联表
 * </p>
 *
 * @author ciky
 * @since 2025-12-16 15:56:01
 */
@Data
@TableName("sys_user_role")
public class SysUserRole {

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private String roleId;

    /**
     * 创建人ID
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人ID
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标识(0-未删除 1-已删除)
     */
    @TableField("delflag")
    private Boolean delflag;

}
