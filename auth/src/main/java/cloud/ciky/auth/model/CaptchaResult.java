package cloud.ciky.auth.model;

import lombok.Builder;
import lombok.Data;

/**
 * <p>
 * 验证码响应对象
 * </p>
 *
 * @author ciky
 * @since 2025/12/11 16:51
 */

@Builder
@Data
public class CaptchaResult {

    /**
     * 验证码唯一标识(用于从Redis获取验证码Code)
     */
    private String captchaKey;

    /**
     * 验证码图片Base64字符串
     */
    private String captchaBase64;
}
