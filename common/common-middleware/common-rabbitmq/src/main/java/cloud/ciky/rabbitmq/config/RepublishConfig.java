package cloud.ciky.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * MQ消息消费失败策略
 * </p>
 *
 * @author ciky
 * @since 2025/12/12 12:15
 */
@Configuration
public class RepublishConfig {
    /**
     * 创建错误的交换机: 用于路由消费不了的消息
     */
    @Bean
    public DirectExchange errorMessageExchange() {
        return new DirectExchange("error.direct");
    }

    /**
     * 创建错误队列: 用于存放消费不了的消息
     */
    @Bean
    public Queue errorQueue() {
        return new Queue("error.queue", true);
    }

    /**
     * 将错误队列绑定到错误交换机上,并设置路由规则
     */
    @Bean
    public Binding errorBinding() {
        return BindingBuilder.bind(errorQueue()).to(errorMessageExchange()).with("error");
    }

    /**
     * 当本地重试次数耗尽时,调用此对象
     * 此对象可以将处理不了的消息投递到错误的交换机上,并路由到错误队列中进行存储
     */
    @Bean
    public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, "error.direct", "error");
    }
}
