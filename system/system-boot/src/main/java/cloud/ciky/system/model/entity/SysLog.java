package cloud.ciky.system.model.entity;

import cloud.ciky.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * <p>
 * 系统日志表
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 15:30:08
 */
@Data
@TableName("sys_log")
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 日志模块
     */
    @TableField("module")
    private String module;

    /**
     * 请求方式
     */
    @TableField("request_method")
    private String requestMethod;

    /**
     * 请求参数(批量请求参数可能会超过text)
     */
    @TableField("request_params")
    private String requestParams;

    /**
     * 返回参数
     */
    @TableField("response_content")
    private String responseContent;

    /**
     * 日志内容
     */
    @TableField("content")
    private String content;

    /**
     * 请求路径
     */
    @TableField("request_uri")
    private String requestUri;

    /**
     * 方法名
     */
    @TableField("method")
    private String method;

    /**
     * IP地址
     */
    @TableField("ip")
    private String ip;

    /**
     * 省份
     */
    @TableField("province")
    private String province;

    /**
     * 城市
     */
    @TableField("city")
    private String city;

    /**
     * 执行时间(ms)
     */
    @TableField("execution_time")
    private Long executionTime;

    /**
     * 浏览器
     */
    @TableField("browser")
    private String browser;

    /**
     * 浏览器版本
     */
    @TableField("browser_version")
    private String browserVersion;

    /**
     * 终端系统
     */
    @TableField("os")
    private String os;

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
     * 逻辑删除标识(1-已删除 0-未删除)
     */
    @TableField("delflag")
    private Boolean delflag;

}
