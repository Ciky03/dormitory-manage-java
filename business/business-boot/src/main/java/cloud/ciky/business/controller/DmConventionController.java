package cloud.ciky.business.controller;

import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.result.Result;
import cloud.ciky.business.model.form.DmConventionAckForm;
import cloud.ciky.business.model.form.DmConventionForm;
import cloud.ciky.business.model.vo.DmConventionAckStateVO;
import cloud.ciky.business.model.vo.DmConventionVO;
import cloud.ciky.business.model.vo.HistoryConventionVO;
import cloud.ciky.business.service.DmConventionAckService;
import cloud.ciky.business.service.DmConventionService;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.core.annotation.RepeatSubmit;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 宿舍公约版本表 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2026-03-24 19:32:06
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/dormitory/convention")
public class DmConventionController {

    private final DmConventionService conventionService;
    private final DmConventionAckService conventionAckService;

    @Operation(summary = "获取当前生效宿舍公约")
    @GetMapping("/room/current")
    public Result<DmConventionVO> getCurrentConvention() {
        DmConventionVO result = conventionService.getCurrentConvention();
        return Result.success(result);
    }

    @Operation(summary = "获取宿舍公约历史/草稿")
    @GetMapping("/history/{roomId}")
    public Result<List<HistoryConventionVO>> getHistoryConvention(@PathVariable String roomId) {
        List<HistoryConventionVO> result = conventionService.getHistoryConvention(roomId);
        return Result.success(result);
    }

    @Operation(summary = "回显宿舍公约")
    @GetMapping("/form/{id}")
    public Result<DmConventionVO> getConventionForm(@PathVariable String id) {
        DmConventionVO result = conventionService.getConventionForm(id);
        return Result.success(result);
    }

    @Operation(summary = "新增宿舍公约草稿")
    @Log(value = "新增宿舍公约草稿", module = LogModuleEnum.CONVENTION)
    @RepeatSubmit
    @PostMapping("/add")
    public Result<Void> addConvention(@Validated @RequestBody DmConventionForm form) {
        boolean result = conventionService.saveConvention(form);
        return Result.judge(result);
    }

    @Operation(summary = "编辑宿舍公约草稿")
    @Log(value = "编辑宿舍公约草稿", module = LogModuleEnum.CONVENTION)
    @RepeatSubmit
    @PutMapping("/edit/{id}")
    public Result<Void> editConvention(@PathVariable String id,
                                       @Validated @RequestBody DmConventionForm form) {
        form.setId(id);
        boolean result = conventionService.saveConvention(form);
        return Result.judge(result);
    }

    @Operation(summary = "发布宿舍公约草稿")
    @Log(value = "发布宿舍公约草稿", module = LogModuleEnum.CONVENTION)
    @RepeatSubmit
    @PostMapping("/publish/{id}")
    public Result<Void> publishConvention(@PathVariable String id) {
        boolean result = conventionService.publishConvention(id);
        return Result.judge(result);
    }

    @Operation(summary = "确认已读并同意宿舍公约")
    @Log(value = "确认已读并同意宿舍公约", module = LogModuleEnum.CONVENTION)
    @RepeatSubmit
    @PostMapping("/ack")
    public Result<Void> ackConvention(@Validated @RequestBody DmConventionAckForm form) {
        boolean result = conventionAckService.ackConvention(form);
        return Result.judge(result);
    }

    @Operation(summary = "获取宿舍公约同意情况")
    @GetMapping("/ack/state/{id}")
    public Result<DmConventionAckStateVO> getAckStat(@PathVariable String id) {
        DmConventionAckStateVO statVO = conventionAckService.getAckStat(id);
        return Result.success(statVO);
    }

}
