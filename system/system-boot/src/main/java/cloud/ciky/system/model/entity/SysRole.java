package cloud.ciky.system.model.entity;

import cloud.ciky.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author ciky
 * @since 2025-12-16 15:56:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
public class SysRole extends BaseEntity {

    /**
     * 角色名称
     */
    @TableField("name")
    private String name;

    /**
     * 角色编码
     */
    @TableField("code")
    private String code;

    /**
     * 角色描述
     */
    @TableField("remark")
    private String remark;

    /**
     * 显示顺序（数字越小排名越靠前）
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 角色状态(1-正常 0-停用)
     */
    @TableField("status")
    private Boolean status;

    /**
     * 数据权限(0-所有数据 1-楼栋 2-宿舍 3-学生 4-无权限)
     */
    @TableField("data_scope")
    private Integer dataScope;

}
