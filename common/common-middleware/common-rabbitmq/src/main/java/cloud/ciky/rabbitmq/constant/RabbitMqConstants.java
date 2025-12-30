package cloud.ciky.rabbitmq.constant;


/**
 * RabbitMQ 常量
 */
public interface RabbitMqConstants {

    String TOPIC_EXCHANGE = "zd.topic.exchange";
    String DEAD_EXCHANGE = "zd.dead.exchange";

    /**
     * 流程消息推送
     */
    interface FlowablePushMessage {
        String FLOWABLE_PUSH_MESSAGE_QUEUE = "zd.flowable.push.message";

        String FLOWABLE_PUSH_MESSAGE_ROUTING_KEY = "zd.flowable.push.message";

        String FLOWABLE_DEAD_PUSH_MESSAGE_QUEUE = "zd.flowable.dead.push.message";
        String FLOWABLE_DEAD_PUSH_MESSAGE_ROUTING_KEY = "zd.flowable.dead.push.message";
    }

    /**
     * 流程审批结果
     */
    interface FlowableApplyResult {
        String FLOWABLE_APPLY_RESULT_EHR_QUEUE = "zd.flowable.apply.result.ehr";
        String FLOWABLE_APPLY_RESULT_SYS_QUEUE = "zd.flowable.apply.result.sys";
        String FLOWABLE_APPLY_RESULT_PMS_QUEUE = "zd.flowable.apply.result.pms";

        String FLOWABLE_APPLY_RESULT_ROUTING_KEY = "zd.flowable.apply.result";

        String FLOWABLE_DEAD_APPLY_RESULT_QUEUE = "zd.flowable.dead.apply.result";
        String FLOWABLE_DEAD_APPLY_RESULT_ROUTING_KEY = "zd.flowable.dead.apply.result";
    }

}
