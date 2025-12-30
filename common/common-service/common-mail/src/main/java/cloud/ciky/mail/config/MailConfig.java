package cloud.ciky.mail.config;

import cloud.ciky.mail.config.property.MailProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * <p>
 * MailConfig 配置类，用于手动配置和注入 JavaMailSender。
 * 通过读取 MailProperties 类中配置的邮件相关属性来初始化 JavaMailSender。
 * 手动注入的原因是为了避免在使用 application-dev.yml 或其他非 application.yml 配置文件时，
 * IDEA 提示无法找到 JavaMailSender 的 bean。
 * </p>
 *
 * @author ciky
 * @since 2025/12/10 11:07
 */
@Configuration
@EnableConfigurationProperties(MailProperties.class)
@ConditionalOnProperty(prefix = "spring.mail", name = "host")
public class MailConfig {

    private final MailProperties mailProperties;

    public MailConfig(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    /**
     * 创建并配置 JavaMailSender bean。
     *
     * @return 配置好的 JavaMailSender 实例
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());
        mailSender.setDefaultEncoding(mailProperties.getDefaultEncoding());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.auth", mailProperties.getProperties().getMail().getSmtp().isAuth());
        properties.put("mail.smtp.starttls.enable", mailProperties.getProperties().getMail().getSmtp().getStarttls().isEnable());
        properties.put("mail.smtp.ssl.enable", mailProperties.getProperties().getMail().getSmtp().getSsl().isEnable());
        properties.put("mail.debug", mailProperties.getProperties().getMail().getDebug());

        return mailSender;
    }
}
