package cloud.ciky.system.api.fallback;

import cloud.ciky.base.result.Result;
import cloud.ciky.system.api.LogFeignClient;
import cloud.ciky.system.model.form.LogForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 系统日志服务远程调用异常后的降级处理类
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 15:46
 */
@Component
@Slf4j
public class LogFeignFallbackClient implements LogFeignClient {

    private static final String FALLBACK_MSG = "feign远程调用系统日志服务异常后的降级方法";

    @Override
    public Result<Void> saveLog(LogForm form) {
        log.error(FALLBACK_MSG);
        return Result.failed();
    }
}
