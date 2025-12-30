package cloud.ciky.sms.service;


/**
 * <p>
 * 短信服务接口层
 * </p>
 *
 * @author ciky
 * @since 2025/12/10 11:12
 */
public interface SmsService {

    /**
     * 发送短信验证码
     *
     * @param mobile        手机号 13388886666
     * @param templateCode  短信模板 SMS_194640010
     * @param templateParam 模板参数 "[{"code":"123456"}]"
     * @return boolean 是否发送成功
     */
    boolean sendSms(String mobile, String templateCode, String templateParam);


}
