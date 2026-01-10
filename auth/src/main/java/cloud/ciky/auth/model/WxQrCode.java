package cloud.ciky.auth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 微信二维码
 * </p>
 *
 * @author ciky
 * @since 2026-01-10 12:06
 */
@Data
public class WxQrCode {

    @Schema(description = "认证url")
    private String url;

    @Schema(description = "绑定微信token")
    private String bindToken;

    @Schema(description = "二维码url")
    private String qrCodeUrl;

    @Schema(description = "登录微信token")
    private String loginToken;

    @Schema(description = "过期时间")
    private int expireSeconds;

}
