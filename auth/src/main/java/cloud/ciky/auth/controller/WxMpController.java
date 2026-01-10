package cloud.ciky.auth.controller;

import cloud.ciky.auth.model.WxQrCode;
import cloud.ciky.auth.service.WxMsgService;
import cloud.ciky.base.result.Result;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 微信公众号 控制器
 * </p>
 *
 * @author ciky
 * @since 2026-01-10 1:06
 */
@Slf4j
@RestController
@RequestMapping("/wx/mp")
@RequiredArgsConstructor
public class WxMpController {

    private final WxMsgService wxMsgService;

    @Schema(description = "获取二维码信息(绑定微信)")
    @GetMapping("/bind/qrCode/info")
    public Result<WxQrCode> getBindQrCodeInfo() {
        WxQrCode qrCode = wxMsgService.getBindQrCodeInfo();
        return Result.success(qrCode);
    }

    @Schema(description = "获取二维码扫码状态(绑定微信)")
    @GetMapping("/bind/qrCode/status")
    public Result<String> getQrCodeStatus(@RequestParam String bindToken) {
        String result =  wxMsgService.getQrCodeStatus(bindToken);
        return Result.success(result);
    }

    @GetMapping("/bind/callback")
    public String bindCallBack(@RequestParam String code, @RequestParam String state) throws WxErrorException {
        return wxMsgService.bindCallBack(code, state);
    }

}
