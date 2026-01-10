package cloud.ciky.auth.enums;

import cloud.ciky.base.IBaseEnum;
import lombok.Getter;

/**
 * <p>
 * 微信公众号场景id枚举
 * </p>
 *
 * @author ciky
 * @since 2026-01-10 14:10
 */
public enum WxMpSceneIdEnum implements IBaseEnum<String> {

    BIND("101", "绑定"),

    LOGIN("201", "登录");


    @Getter
    private String value;

    @Getter
    private String label;

    WxMpSceneIdEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
