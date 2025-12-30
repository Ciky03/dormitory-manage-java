package cloud.ciky.rabbitmq.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * MQ切面获取token已经tenantId
 * </p>
 *
 * @author ciky
 * @since 2025/12/9 14:26
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithSecurityContextFromMessage {

}