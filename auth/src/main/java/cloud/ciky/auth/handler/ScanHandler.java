package cloud.ciky.auth.handler;

import cloud.ciky.auth.enums.WxMpSceneIdEnum;
import cloud.ciky.auth.service.WxMsgService;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * <p>
 * 扫码事件处理器
 * </p>
 *
 * @author ciky
 * @since 2026-01-10 1:20
 */
@Component
@RequiredArgsConstructor
public class ScanHandler implements WxMpMessageHandler {

    private final WxMsgService wxMsgService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        // 解析eventKey
        String eventKey = wxMpXmlMessage.getEventKey();
        String sceneId = eventKey.replace("qrscene_", "");

        // 101: 绑定微信
        if(sceneId.equals(WxMpSceneIdEnum.BIND.getValue())){
            return wxMsgService.bindScan(wxMpXmlMessage);
        }

        return null;
    }
}
