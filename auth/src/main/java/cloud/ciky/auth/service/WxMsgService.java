package cloud.ciky.auth.service;

import cloud.ciky.auth.config.WxMpConfig;
import cloud.ciky.auth.enums.WxMpSceneIdEnum;
import cloud.ciky.auth.enums.WxMpStatusEnum;
import cloud.ciky.auth.model.WxQrCode;
import cloud.ciky.base.constant.RedisConstants;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.base.result.Result;
import cloud.ciky.security.util.SecurityUtils;
import cloud.ciky.system.api.UserFeignClient;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 微信事件处理 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-01-10 15:31
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WxMsgService {

    private final StringRedisTemplate redisTemplate;
    private final WxMpService wxMpService;
    private final WxMpConfig wxMpConfig;
    private final UserFeignClient userFeignClient;

    /**
     * 二维码 Ticket 过期时间(秒)
     */
    private static final int QR_CODE_TICKET_TIMEOUT = 300;

    /**
     * 获取绑定微信key
     */
    @NotNull
    private static String getBindRedisKey(String bindToken) {
        return RedisConstants.WxMp.BIND_TOKEN + bindToken;
    }

    /**
     * <p>
     * (绑定微信)
     * 获取公众号二维码信息(二维码url, bindToken, 过期时间)
     * </p>
     *
     * @return cloud.ciky.auth.model.WxQrCode
     * @author ciky
     * @since 2026/1/10 16:08
     */
    public WxQrCode getBindQrCodeInfo() {
        try {
            WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(WxMpSceneIdEnum.BIND.getValue(), QR_CODE_TICKET_TIMEOUT);
            String ticketStr = ticket.getTicket();
            log.info("申请二维码的ticket: {}", ticketStr);
            String url = wxMpService.getQrcodeService().qrCodePictureUrl(ticketStr);
            log.info("申请的微信公众号二维码: {}", url);

            // 存redis
            String userId = SecurityUtils.getUserId();
            String key = getBindRedisKey(ticketStr);
            redisTemplate.opsForHash().put(key, "userId", userId);
            redisTemplate.opsForHash().put(key, "status", WxMpStatusEnum.PENDING.getValue());
            redisTemplate.opsForHash().put(key, "createTime", String.valueOf(System.currentTimeMillis()));
            redisTemplate.expire(key, QR_CODE_TICKET_TIMEOUT, TimeUnit.SECONDS);

            WxQrCode qrCode = new WxQrCode();
            qrCode.setQrCodeUrl(url);
            qrCode.setExpireSeconds(QR_CODE_TICKET_TIMEOUT);
            qrCode.setBindToken(ticketStr);

            return qrCode;
        } catch (WxErrorException e) {
            log.error("申请微信公众号二维码失败: {}", e.getMessage());
            throw new BusinessException("申请微信公众号二维码失败");
        }
    }

    /**
     * <p>
     * 扫码事件
     * </p>
     *
     * @author ciky
     * @since 2026/1/10 16:11
     */
    public WxMpXmlOutMessage bindScan(WxMpXmlMessage wxMpXmlMessage) {
        String openId = wxMpXmlMessage.getFromUser();
        log.info("用户的openId:{}", openId);

        String ticket = wxMpXmlMessage.getTicket();
        log.info("二维码ticket:{}", ticket);

        // 从redis拿userId
        String key = getBindRedisKey(ticket);
        String userId = Optional.ofNullable(redisTemplate.opsForHash().get(key, "userId")).orElse("").toString();
        String content = null;
        if (CharSequenceUtil.isNotBlank(userId)) {
            // 绑定openId到user表
            userFeignClient.bindWxMp(userId, openId);
            // 更新redis
            redisTemplate.opsForHash().put(key, "status", WxMpStatusEnum.CONFIRMED.getValue());
            content = "绑定成功!";
            log.info("用户{}绑定成功,openId:{}", userId, openId);
        } else {
            redisTemplate.opsForHash().put(key, "status", WxMpStatusEnum.EXPIRED.getValue());
            content = "绑定失败, 请重新扫码二维码!";
            log.error("用户{}绑定失败,openId:{}", userId, openId);
        }

        // 向用户返回授权链接
        return WxMpXmlOutMessage.TEXT().content(content)
                .fromUser(wxMpXmlMessage.getToUser())
                .toUser(wxMpXmlMessage.getFromUser())
                .build();
    }

    /**
     * <p>
     * 获取绑定微信二维码状态 (前端轮询)
     * </p>
     *
     * @param bindToken 二维码ticket
     * @author ciky
     * @since 2026/1/10 16:41
     */
    public String getQrCodeStatus(String bindToken) {
        String key = getBindRedisKey(bindToken);
        return Optional.ofNullable(redisTemplate.opsForHash().get(key, "status")).orElse(WxMpStatusEnum.EXPIRED.getValue()).toString();
    }

    /**
     * <p>
     * 获取绑定认证信息 (认证url, bindToken, 过期时间)
     * </p>
     *
     * @return cloud.ciky.auth.model.WxQrCode
     * @author ciky
     * @since 2026/1/10 15:40
     */
    public WxQrCode getBindAuthInfo() {
        String userId = SecurityUtils.getUserId();
        // 生成bindToken
        String bindToken = UUID.randomUUID().toString().replace("-", "");
        // 存redis
        String key = getBindRedisKey(bindToken);
        redisTemplate.opsForHash().put(key, "userId", userId);
        redisTemplate.opsForHash().put(key, "status", WxMpStatusEnum.PENDING.getValue());
        redisTemplate.opsForHash().put(key, "createTime", String.valueOf(System.currentTimeMillis()));
        redisTemplate.expire(key, QR_CODE_TICKET_TIMEOUT, TimeUnit.SECONDS);

        // 认证url
        String url = wxMpService.getOAuth2Service().buildAuthorizationUrl(
                getBindCallBackUrl(),
                WxConsts.OAuth2Scope.SNSAPI_BASE,
                bindToken
        );

        WxQrCode qrCode = new WxQrCode();
        qrCode.setUrl(url);
        qrCode.setExpireSeconds(QR_CODE_TICKET_TIMEOUT);
        qrCode.setBindToken(bindToken);
        return qrCode;
    }

    /**
     * 绑定微信回调url
     */
    @NotNull
    private String getBindCallBackUrl() {
        return wxMpConfig.getCallback() + "/auth/wx/mp/bind/callback";
    }


    /**
     * <p>
     * "授权"连接后,回调此方法
     * 1. 通过code换取access_token
     * 2. 通过access_token获取openId
     * </p>
     *
     * @param code  微信公众号code
     * @param state 绑定token
     * @author ciky
     * @since 2026/1/10 15:38
     */
    public String bindCallBack(String code, String state) throws WxErrorException {
        log.info("回调code:{}, state:{}", code, state);
        // 获取openId
        WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
        String openId = accessToken.getOpenId();
        log.info("获取到的openId:{}", openId);
        // 从redis拿userId
        String key = getBindRedisKey(state);
        String userId = Optional.ofNullable(redisTemplate.opsForHash().get(key, "userId")).orElse("").toString();
        // TODO 绑定openId到user表

        // 更新redis
        redisTemplate.opsForHash().put(key, "status", WxMpStatusEnum.CONFIRMED.getValue());
        return "认证成功!";
    }


}
