package cloud.ciky.rabbitmq.aspect;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.amqp.core.Message;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityContextFromMessageAspect {

    private final JwtDecoder jwtDecoder;

    @Around("@annotation(withSecurity) && args(message, ..)")
    public Object setupSecurityContext(ProceedingJoinPoint joinPoint,
                                       WithSecurityContextFromMessage withSecurity,
                                       Message message) throws Throwable {

        String originalToken = null;
        try {
            // 1. 从消息头获取 Authorization
            Object authHeader = message.getMessageProperties().getHeaders().get("Authorization");
            if (authHeader instanceof String tokenStr && CharSequenceUtil.isNotBlank(tokenStr)) {
                if (tokenStr.startsWith("Bearer ")) {
                    originalToken = tokenStr.substring(7);
                } else {
                    originalToken = tokenStr;
                }

                // 2. 解码 JWT
                Jwt jwt = jwtDecoder.decode(originalToken);
                // 3. 创建 Authentication 并设置到上下文
                JwtAuthenticationToken authentication = new JwtAuthenticationToken(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("SecurityContext set from message token for method: {}", joinPoint.getSignature().getName());
            } else {
                log.info("No valid 'Authorization' header in message for method: {}", joinPoint.getSignature().getName());
            }

            // 4. 执行原方法
            return joinPoint.proceed();

        } catch (Exception e) {
            throw e;
        } finally {
            SecurityContextHolder.clearContext();
        }
    }
}