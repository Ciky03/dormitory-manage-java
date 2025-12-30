package cloud.ciky.mail.service.impl;

import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.mail.config.property.MailProperties;
import cloud.ciky.mail.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.angus.mail.smtp.SMTPAddressFailedException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * <p>
 * 邮件服务实现类
 * </p>
 *
 * @author ciky
 * @since 2025/12/10 11:09
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    private final MailProperties mailProperties;

    /**
     * 发送简单文本邮件
     *
     * @param to      收件人地址
     * @param subject 邮件主题
     * @param text    邮件内容
     */
    @Override
    public void sendMail(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress(mailProperties.getFrom(), mailProperties.getFromName()));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("发送邮件失败:{}, 错误信息:{}", to, e.getMessage());
            throw new BusinessException("发送邮件失败");
        }
    }

    /**
     * 发送带附件的邮件
     *
     * @param to       收件人地址
     * @param subject  邮件主题
     * @param text     邮件内容
     * @param filePath 附件路径
     */
    @Override
    public void sendMailWithAttachment(String to, String subject, String text, String filePath) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mailProperties.getFrom());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);  // true表示支持HTML内容

            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(file.getFilename(), file);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("发送邮件失败:{}, 错误信息:{}", to, e.getMessage());
            throw new BusinessException("发送邮件失败");
        }
    }

    @Override
    public void sendHtmlMail(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress(mailProperties.getFrom(), mailProperties.getFromName()));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);  // true表示支持HTML内容

            mailSender.send(message);
        } catch (MailException | SMTPAddressFailedException e) {
            log.error("发送邮件失败:{}, 错误信息:{}", to, e.getMessage());
        } catch (Exception e) {
            log.error("发送邮件失败:{}, 错误信息:{}", to, e.getMessage());
            throw new BusinessException("发送邮件失败");
        }
    }


    @Override
    public void sendHtmlMailThrowExc(String to, String subject, String text) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(new InternetAddress(mailProperties.getFrom(), mailProperties.getFromName()));
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);  // true表示支持HTML内容

        mailSender.send(message);
    }
}
