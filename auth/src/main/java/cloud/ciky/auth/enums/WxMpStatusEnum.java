package cloud.ciky.auth.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 微信公众号二维码状态枚举
 * </p>
 *
 * @author ciky
 * @since 2026/1/10 15:42
 */
@AllArgsConstructor
public enum WxMpStatusEnum implements IBaseEnum<String> {

    PENDING("PENDING", "等待中"),
    CONFIRMED("CONFIRMED", "已完成"),
    EXPIRED("EXPIRED","已过期");

    @Getter
    private String value;

    @Getter
    private String label;

}
