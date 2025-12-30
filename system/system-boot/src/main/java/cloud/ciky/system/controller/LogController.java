package cloud.ciky.system.controller;

import cloud.ciky.base.result.Result;
import cloud.ciky.system.model.entity.SysLog;
import cloud.ciky.system.model.form.LogForm;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.ciky.system.service.SysLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 15:30:08
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/log")
public class LogController {

    private final SysLogService logService;

    @Operation(summary = "日志记录")
    @PostMapping("/save")
    public Result<Void> saveLog(@RequestBody LogForm form) {
        SysLog entity = BeanUtil.copyProperties(form, SysLog.class);
        logService.save(entity);
        return Result.success();
    }
}
