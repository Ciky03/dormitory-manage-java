package cloud.ciky.business.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>
 * 宿管楼栋关联表
 * </p>
 *
 * @author ciky
 * @since 2026-03-19 16:43:49
 */
@Data
@TableName("building_dm")
public class BuildingDm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 楼栋id
     */
    @TableField("building_id")
    private String buildingId;

    /**
     * 宿管id
     */
    @TableField("dm_id")
    private String dmId;

    /**
     * 是否当前宿管(1-当前 0-历史)
     */
    @TableField("is_current")
    private Boolean isCurrent;

    /**
     * 任职开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 任职结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;


}
