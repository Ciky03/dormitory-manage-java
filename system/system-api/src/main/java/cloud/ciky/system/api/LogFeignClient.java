package cloud.ciky.system.api;

import cloud.ciky.base.result.Result;
import cloud.ciky.core.config.FeignDecoderConfig;
import cloud.ciky.system.api.fallback.LogFeignFallbackClient;
import cloud.ciky.system.model.form.LogForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 远程调用系统日志服务
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 15:46
 */
@FeignClient(value = "system", contextId = "log", fallback = LogFeignFallbackClient.class, configuration = {FeignDecoderConfig.class})
public interface LogFeignClient {

    @PostMapping("/log/save")
    Result<Void> saveLog(@RequestBody LogForm form);
}
