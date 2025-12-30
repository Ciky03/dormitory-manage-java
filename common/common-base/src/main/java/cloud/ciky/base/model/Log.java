package cloud.ciky.base.model;

import cloud.ciky.base.enums.LogModuleEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统日志数据模型
 * </p>
 *
 * @author ciky
 * @since 2025/12/9 12:03
 */
@Data
public class Log implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 日志模块
     */
    private LogModuleEnum module;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应参数
     */

    private String responseContent;

    /**
     * 日志内容
     */
    private String content;

    /**
     * 请求路径
     */
    private String requestUri;

    /**
     * IP 地址
     */
    private String ip;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 浏览器版本
     */
    private String browserVersion;

    /**
     * 终端系统
     */
    private String os;

    /**
     * 执行时间(毫秒)
     */
    private Long executionTime;

    /**
     * 创建人ID
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;


}