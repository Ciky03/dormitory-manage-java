package cloud.ciky.business.controller;

import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.result.PageResult;
import cloud.ciky.base.result.Result;
import cloud.ciky.business.model.form.DmSharedCostForm;
import cloud.ciky.business.model.form.DmSharedCostPayForm;
import cloud.ciky.business.model.query.DmSharedCostPageQuery;
import cloud.ciky.business.model.vo.DmSharedCostDetailVO;
import cloud.ciky.business.model.vo.DmSharedCostPageVO;
import cloud.ciky.business.model.vo.DmSharedCostStatVO;
import cloud.ciky.business.service.DmSharedCostService;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.core.annotation.RepeatSubmit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dormitory/cost")
public class DmSharedCostController {

    private final DmSharedCostService sharedCostService;

    @Operation(summary = "获取宿舍费用公摊统计")
    @GetMapping("/stat")
    public Result<DmSharedCostStatVO> getSharedCostStat() {
        return Result.success(sharedCostService.getSharedCostStat());
    }

    @Operation(summary = "获取宿舍费用公摊分页列表")
    @GetMapping("/list")
    public PageResult<DmSharedCostPageVO> listSharedCost(@ParameterObject DmSharedCostPageQuery query) {
        Page<DmSharedCostPageVO> page = sharedCostService.listSharedCost(query);
        return PageResult.success(page);
    }

    @Operation(summary = "获取宿舍费用公摊详情")
    @GetMapping("/detail/{id}")
    public Result<DmSharedCostDetailVO> getSharedCostDetail(@PathVariable String id) {
        return Result.success(sharedCostService.getSharedCostDetail(id));
    }

    @Operation(summary = "新增宿舍费用公摊")
    @Log(value = "新增宿舍费用公摊", module = LogModuleEnum.SHARED_COST)
    @RepeatSubmit
    @PostMapping("/add")
    public Result<Void> addSharedCost(@Validated @RequestBody DmSharedCostForm form) {
        return Result.judge(sharedCostService.saveSharedCost(form));
    }

    @Operation(summary = "编辑宿舍费用公摊")
    @Log(value = "编辑宿舍费用公摊", module = LogModuleEnum.SHARED_COST)
    @RepeatSubmit
    @PutMapping("/edit/{id}")
    public Result<Void> editSharedCost(@PathVariable String id,
                                       @Validated @RequestBody DmSharedCostForm form) {
        form.setId(id);
        return Result.judge(sharedCostService.saveSharedCost(form));
    }

    @Operation(summary = "发布宿舍费用公摊")
    @Log(value = "发布宿舍费用公摊", module = LogModuleEnum.SHARED_COST)
    @RepeatSubmit
    @PostMapping("/publish/{id}")
    public Result<Void> publishSharedCost(@PathVariable String id) {
        return Result.judge(sharedCostService.publishSharedCost(id));
    }

    @Operation(summary = "缴纳宿舍费用公摊")
    @Log(value = "缴纳宿舍费用公摊", module = LogModuleEnum.SHARED_COST)
    @RepeatSubmit
    @PostMapping("/pay/{detailId}")
    public Result<Void> paySharedCost(@PathVariable String detailId,
                                      @Validated @RequestBody DmSharedCostPayForm form) {
        return Result.judge(sharedCostService.paySharedCost(detailId, form));
    }

    @Operation(summary = "取消宿舍费用公摊")
    @Log(value = "取消宿舍费用公摊", module = LogModuleEnum.SHARED_COST)
    @RepeatSubmit
    @PostMapping("/cancel/{id}")
    public Result<Void> cancelSharedCost(@PathVariable String id) {
        return Result.judge(sharedCostService.cancelSharedCost(id));
    }

    @Operation(summary = "删除宿舍费用公摊")
    @Log(value = "删除宿舍费用公摊", module = LogModuleEnum.SHARED_COST)
    @DeleteMapping("/del/{id}")
    public Result<Void> deleteSharedCost(@PathVariable String id) {
        return Result.judge(sharedCostService.deleteSharedCost(id));
    }
}
