package cloud.ciky.business.model.entity;

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
 * 宿舍公约版本表
 * </p>
 *
 * @author ciky
 * @since 2026-03-24 19:32:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dm_convention")
public class DmConvention extends BaseEntity {

    /**
     * 宿舍ID
     */
    @TableField("room_id")
    private String roomId;

    /**
     * 版本号(同宿舍递增)
     */
    @TableField("version_no")
    private Integer versionNo;

    /**
     * 是否当前版本:1-是 0-否
     */
    @TableField("is_current")
    private Boolean isCurrent;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 公约内容(Markdown)
     */
    @TableField("content_md")
    private String contentMd;

    /**
     * 状态:0-草稿 1-已发布 2-已作废
     */
    @TableField("status")
    private Integer status;

    /**
     * 发布人
     */
    @TableField("publish_by")
    private String publishBy;

    /**
     * 发布时间
     */
    @TableField("publish_time")
    private LocalDateTime publishTime;

}
