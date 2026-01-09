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
 * 系统附件信息
 * </p>
 *
 * @author ciky
 * @since 2026-01-09 00:29:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_attach")
public class SysAttach extends BaseEntity {

    /**
     * 文件md5值
     */
    @TableField("md5")
    private String md5;

    /**
     * 文件名称
     */
    @TableField("name")
    private String name;

    /**
     * 存储目录
     */
    @TableField("bucket")
    private String bucket;

    /**
     * 存储路径
     */
    @TableField("path")
    private String path;

    /**
     * 文件大小
     */
    @TableField("size")
    private Long size;

    /**
     * 媒体类型
     */
    @TableField("mime_type")
    private String mimeType;

}
