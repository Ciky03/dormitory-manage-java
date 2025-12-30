package cloud.ciky.core.aspect;

import cloud.ciky.core.annotation.RepeatSubmit;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.base.result.ResultCode;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.text.CharSequenceUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 防重复提交切面
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
@Aspect
@Component
@RequiredArgsConstructor
public class RepeatSubmitAspect {

    private final RedissonClient redissonClient;

    private static final String RESUBMIT_LOCK_PREFIX = "LOCK:RESUBMIT:";

    /**
     * 防重复提交切点
     */
    @Pointcut("@annotation(repeatSubmit)")
    public void repeatSubmitPointCut(RepeatSubmit repeatSubmit) {
    }

    @Around(value = "repeatSubmitPointCut(repeatSubmit)", argNames = "pjp,repeatSubmit")
    public Object doAround(ProceedingJoinPoint pjp, RepeatSubmit repeatSubmit) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String jti = SecurityUtils.getJti();
        String resubmitLockKey;
        if (CharSequenceUtil.isNotBlank(jti)) {
            resubmitLockKey = RESUBMIT_LOCK_PREFIX + jti + ":" + request.getMethod() + "-" + request.getRequestURI();

        } else {
            String clientIp = request.getRemoteAddr();
            resubmitLockKey = RESUBMIT_LOCK_PREFIX + clientIp + ":" + request.getMethod() + "-" + request.getRequestURI();
        }
        // 防重提交锁过期时间
        int expire = repeatSubmit.expire();
        RLock lock = redissonClient.getLock(resubmitLockKey);
        // 获取锁失败，直接返回 false
        boolean lockResult = lock.tryLock(0, expire, TimeUnit.SECONDS);
        if (!lockResult) {
            // 抛出重复提交提示信息
            throw new BusinessException(ResultCode.REPEAT_SUBMIT_ERROR);
        }

        return pjp.proceed();
    }


}
