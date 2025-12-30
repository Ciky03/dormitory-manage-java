package cloud.ciky.core.aspect;

import cloud.ciky.core.annotation.RedisCacheable;
import cloud.ciky.redis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 自动缓存切面
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */

@Aspect
@Component
@RequiredArgsConstructor
public class RedisCacheAspect {

    private final RedisUtils redisUtils;

    @Around("@annotation(redisCacheable)")
    public Object around(ProceedingJoinPoint joinPoint, RedisCacheable redisCacheable) throws Throwable {
        String key = redisCacheable.key();
        int ttl = redisCacheable.ttl();

        // 1. 先查 Redis
        Object cache = redisUtils.get(key);
        if (cache != null) {
            return cache;
        }

        // 2. 执行业务方法
        Object result = joinPoint.proceed();

        // 3. 写入缓存
        redisUtils.set(key, result, ttl, TimeUnit.SECONDS);

        return result;
    }
}