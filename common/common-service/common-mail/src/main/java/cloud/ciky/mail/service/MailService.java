package cloud.ciky.mail.service;

/**
 * <p>
 * 邮件服务接口层
 * </p>
 *
 * @author ciky
 * @since 2025/12/10 11:09
 */
public interface MailService {


    /**
     * 发送简单文本邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param text    邮件内容
     */
    void sendMail(String to, String subject, String text);

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人地址
     * @param subject  邮件主题
     * @param text     邮件内容
     * @param filePath 附件路径
     */
    void sendMailWithAttachment(String to, String subject, String text, String filePath);


    /**
     * <p>
     * 发送HTML格式的邮件
     * </p>
     *
     * @param to      收件人
     * @param subject 邮件主题
     * @param text    邮件正文
     */
    void sendHtmlMail(String to, String subject, String text);

    /**
     * <p>
     * 发送HTML格式的邮件
     * </p>
     *
     * @param to      收件人
     * @param subject 邮件主题
     * @param text    邮件正文
     * @author ciky
     * @since 2025/8/27 18:06
     */
    void sendHtmlMailThrowExc(String to, String subject, String text) throws Exception;
}
