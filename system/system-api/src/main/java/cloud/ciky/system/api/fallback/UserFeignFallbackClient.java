package cloud.ciky.system.api.fallback;

import cloud.ciky.base.result.Result;
import cloud.ciky.system.api.UserFeignClient;
import cloud.ciky.system.model.dto.UserAuthDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 系统用户服务远程调用异常后的降级处理类
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 17:05
 */
@Component
@Slf4j
public class UserFeignFallbackClient implements UserFeignClient {

    private static final String FALLBACK_MSG = "feign远程调用系统日志服务异常后的降级方法";

    @Override
    public Result<UserAuthDTO> getUserAuthInfo(String authKey) {
        log.error(FALLBACK_MSG);
        return Result.failed();
    }

    @Override
    public Result<Void> bindWxMp(String userId, String openId) {
        log.error(FALLBACK_MSG);
        return Result.failed();
    }
}
