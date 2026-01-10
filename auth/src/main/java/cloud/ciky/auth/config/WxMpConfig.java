package cloud.ciky.auth.config;

import lombok.Data;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * <p>
 * 微信公众号配置
 * </p>
 *
 * @author ciky
 * @since 2026-01-10 1:03
 */
@ConfigurationProperties(prefix = "wx.mp")
@Configuration
@Data
public class WxMpConfig {

    private String appId;

    private String secret;

    private String callback;

    private String token;

    private String aesKey;

    @Bean
    public WxMpService wxMpService() {
        WxMpServiceImpl service = new WxMpServiceImpl();
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(appId);
        config.setSecret(secret);
        config.setToken(token);
        config.setAesKey(aesKey);
        service.setWxMpConfigStorage(config);
        return service;
    }

}
