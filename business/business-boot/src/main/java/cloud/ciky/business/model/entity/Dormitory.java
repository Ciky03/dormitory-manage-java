package cloud.ciky.business.model.entity;

import cloud.ciky.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 宿舍表
 * </p>
 *
 * @author ciky
 * @since 2026-03-11 17:04:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dormitory")
public class Dormitory extends BaseEntity {

    /**
     * 父id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 父路径
     */
    @TableField("tree_path")
    private String treePath;

    /**
     * 楼栋/宿舍号
     */
    @TableField("room_num")
    private String roomNum;

    /**
     * 宿舍人数
     */
    @TableField("capacity")
    private Integer capacity;

}
