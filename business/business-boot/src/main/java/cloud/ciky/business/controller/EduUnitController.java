package cloud.ciky.business.controller;

import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.result.PageResult;
import cloud.ciky.base.result.Result;
import cloud.ciky.business.model.form.EduUnitForm;
import cloud.ciky.business.model.query.ClassPageQuery;
import cloud.ciky.business.model.query.UnitQuery;
import cloud.ciky.business.model.vo.ClassPageVO;
import cloud.ciky.business.model.vo.EduUnitVO;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.core.annotation.RepeatSubmit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.houbb.sensitive.word.support.ignore.AbstractSensitiveWordCharIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cloud.ciky.business.service.EduUnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 学院/专业/班级表 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2026-02-05 17:03:26
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/unit")
public class EduUnitController {

    private final EduUnitService eduUnitService;

    @Operation(summary = "获取学院/专业/班级树")
    @GetMapping("/tree/list")
    public Result<List<EduUnitVO>> listUnitTree(@ParameterObject UnitQuery query) {
        List<EduUnitVO> menuList = eduUnitService.listUnitTree(query);
        return Result.success(menuList);
    }

    @Operation(summary = "获取班级列表")
    @GetMapping("/class/list")
    public PageResult<ClassPageVO> listClass(
            @ParameterObject ClassPageQuery query
    ) {
        Page<ClassPageVO> menuList = eduUnitService.listClass(query);
        return PageResult.success(menuList);
    }

    @Operation(summary = "获取学院/专业/班级表单")
    @GetMapping("/form/{id}")
    public Result<EduUnitForm> getMenuForm(
            @PathVariable String id
    ) {
        EduUnitForm menu = eduUnitService.getUnitForm(id);
        return Result.success(menu);
    }

    @Operation(summary = "新增学院/专业/班级")
    @Log(value = "新增学院/专业/班级", module = LogModuleEnum.UNIT)
    @RepeatSubmit
    @PostMapping("/add")
    public Result<Void> addMenu(@Validated @RequestBody EduUnitForm form) {
        boolean result = eduUnitService.saveUnit(form);
        return Result.judge(result);
    }

    @Operation(summary = "编辑学院/专业/班级")
    @Log(value = "编辑学院/专业/班级", module = LogModuleEnum.UNIT)
    @RepeatSubmit
    @PutMapping("/edit/{id}")
    public Result<Void> editMenu(@Validated @RequestBody EduUnitForm form, @PathVariable String id) {
        form.setId(id);
        boolean result = eduUnitService.saveUnit(form);
        return Result.judge(result);
    }

    @Operation(summary = "删除学院/专业/班级")
    @Log(value = "删除学院/专业/班级", module = LogModuleEnum.UNIT)
    @DeleteMapping("/del/{id}")
    public Result<Void> deleteUnit(
           @PathVariable("id") String id
    ) {
        boolean result = eduUnitService.deleteUnit(id);
        return Result.judge(result);
    }

}
