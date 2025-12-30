package cloud.ciky.core.aspect;


import cloud.ciky.core.annotation.RateLimit;
import cloud.ciky.base.exception.BusinessException;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;


/**
 * <p>
 *  限流切面
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
@Aspect
@Component
public class RateLimitAspect {

    private final ConcurrentHashMap<Object, Object> rateLimiters = new ConcurrentHashMap<>();
    @Around("@annotation(rateLimit)")
    public Object doRateLimit(ProceedingJoinPoint pjp, RateLimit rateLimit) throws Throwable {
        String methodName = pjp.getSignature().getName();
        RateLimiter limiter = (RateLimiter) rateLimiters.computeIfAbsent(methodName,
                k -> RateLimiter.create(rateLimit.permitsPerSecond()));

        if (!limiter.tryAcquire()) {
            throw new BusinessException("接口已被限流");
        }

        return pjp.proceed();
    }
}
