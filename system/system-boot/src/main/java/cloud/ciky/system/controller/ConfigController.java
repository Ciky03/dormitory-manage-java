package cloud.ciky.system.controller;

import cloud.ciky.base.result.Result;
import cloud.ciky.system.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 系统配置 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2026-01-02 18:43
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/config")
public class ConfigController {

    private final ConfigService configService;

    @Operation(summary = "获取排序")
    @GetMapping("/sort/{business}")
    public Result<Long> getSort(@PathVariable String business, String parentId) {
        Long sort = configService.getSort(business, parentId);
        return Result.success(sort);
    }

}
