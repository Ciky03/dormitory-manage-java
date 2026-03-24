package cloud.ciky.business.controller;

import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.result.PageResult;
import cloud.ciky.base.result.Result;
import cloud.ciky.business.model.form.EducationForm;
import cloud.ciky.business.model.query.ClassPageQuery;
import cloud.ciky.business.model.query.EducationQuery;
import cloud.ciky.business.model.vo.ClassPageVO;
import cloud.ciky.business.model.vo.EducationVO;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.core.annotation.RepeatSubmit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import cloud.ciky.business.service.EducationService;
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
@RequestMapping("/edu")
public class EducationController {

    private final EducationService educationService;

    @Operation(summary = "获取学院/专业/班级树")
    @GetMapping("/tree/list")
    public Result<List<EducationVO>> listEducationTree(@ParameterObject EducationQuery query) {
        List<EducationVO> EducationList = educationService.listEducationTree(query);
        return Result.success(EducationList);
    }

    @Operation(summary = "获取班级列表")
    @GetMapping("/class/list")
    public PageResult<ClassPageVO> listClass(
            @ParameterObject ClassPageQuery query
    ) {
        Page<ClassPageVO> EducationList = educationService.listClass(query);
        return PageResult.success(EducationList);
    }

    @Operation(summary = "获取学院/专业/班级表单")
    @GetMapping("/form/{id}")
    public Result<EducationForm> getEducationForm(
            @PathVariable String id
    ) {
        EducationForm Education = educationService.getEducationForm(id);
        return Result.success(Education);
    }

    @Operation(summary = "新增学院/专业/班级")
    @Log(value = "新增学院/专业/班级", module = LogModuleEnum.EDUCATION)
    @RepeatSubmit
    @PostMapping("/add")
    public Result<Void> addEducation(@Validated @RequestBody EducationForm form) {
        boolean result = educationService.saveEducation(form);
        return Result.judge(result);
    }

    @Operation(summary = "编辑学院/专业/班级")
    @Log(value = "编辑学院/专业/班级", module = LogModuleEnum.EDUCATION)
    @RepeatSubmit
    @PutMapping("/edit/{id}")
    public Result<Void> editEducation(@Validated @RequestBody EducationForm form, @PathVariable String id) {
        form.setId(id);
        boolean result = educationService.saveEducation(form);
        return Result.judge(result);
    }

    @Operation(summary = "删除学院/专业/班级")
    @Log(value = "删除学院/专业/班级", module = LogModuleEnum.EDUCATION)
    @DeleteMapping("/del/{id}")
    public Result<Void> deleteEducation(
           @PathVariable("id") String id
    ) {
        boolean result = educationService.deleteEducation(id);
        return Result.judge(result);
    }

}
