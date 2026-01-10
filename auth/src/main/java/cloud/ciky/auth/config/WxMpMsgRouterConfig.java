package cloud.ciky.auth.config;

import cloud.ciky.auth.handler.ScanHandler;
import cloud.ciky.auth.handler.SubscribeHandler;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static me.chanjar.weixin.common.api.WxConsts.EventType.SCAN;
import static me.chanjar.weixin.common.api.WxConsts.EventType.SUBSCRIBE;
import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;

/**
 * <p>
 * 微信公众号消息路由配置类
 * </p>
 *
 * @author ciky
 * @since 2026-01-10 16:19
 */
@Component
@RequiredArgsConstructor
public class WxMpMsgRouterConfig {

    private final ScanHandler scanHandler;

    private final SubscribeHandler subscribeHandler;

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService){
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);

        // 关注事件
        newRouter.rule().async(false).msgType(EVENT).event(SUBSCRIBE).handler(this.subscribeHandler).end();
        // 扫码事件
        newRouter.rule().async(false).msgType(EVENT).event(SCAN).handler(this.scanHandler).end();

        return newRouter;
    }
}
