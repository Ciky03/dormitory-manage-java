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
 * 菜单管理
 * </p>
 *
 * @author ciky
 * @since 2026-01-01 17:27:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
public class SysMenu extends BaseEntity {

    /**
     * 父菜单ID
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 节点路径
     */
    @TableField("tree_path")
    private String treePath;

    /**
     * 菜单名称
     */
    @TableField("name")
    private String name;

    /**
     * 菜单类型（1-菜单 2-目录 3-外链 4-按钮）
     */
    @TableField("type")
    private Integer type;

    /**
     * 路由名称（Vue Router 中用于命名路由）
     */
    @TableField("route_name")
    private String routeName;

    /**
     * 路由路径（Vue Router 中定义的 URL 路径）
     */
    @TableField("route_path")
    private String routePath;

    /**
     * 组件路径（组件页面完整路径，相对于 src/views/，缺省后缀 .vue）
     */
    @TableField("component")
    private String component;

    /**
     * 【按钮】权限标识
     */
    @TableField("perm")
    private String perm;

    /**
     * 【菜单】是否开启页面缓存（1-是 0-否）
     */
    @TableField("keep_alive")
    private Boolean keepAlive;

    /**
     * 显示状态（1-显示 0-隐藏）
     */
    @TableField("visible")
    private Boolean visible;

    /**
     * 显示顺序（数字越小排名越靠前）
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 未选中菜单图标
     */
    @TableField("icon")
    private String icon;

    /**
     * 选中菜单图标
     */
    @TableField("pitch_icon")
    private String pitchIcon;

}
