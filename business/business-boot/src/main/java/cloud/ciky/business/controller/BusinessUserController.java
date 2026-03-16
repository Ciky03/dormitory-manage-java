package cloud.ciky.business.controller;

import cloud.ciky.base.BaseQuery;
import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.result.PageResult;
import cloud.ciky.base.result.Result;
import cloud.ciky.business.model.form.UserDormitoryManagerForm;
import cloud.ciky.business.model.form.UserStudentForm;
import cloud.ciky.business.model.form.UserTeacherForm;
import cloud.ciky.business.model.query.StudentPageQuery;
import cloud.ciky.business.model.vo.DormitoryManagerPageVO;
import cloud.ciky.business.model.vo.StudentPageVO;
import cloud.ciky.business.model.vo.TeacherPageVO;
import cloud.ciky.business.service.UserDormitoryManagerService;
import cloud.ciky.business.service.UserStudentService;
import cloud.ciky.business.service.UserTeacherService;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.core.annotation.RepeatSubmit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 学生表 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2026-03-12 17:53:23
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/person")
public class BusinessUserController {

    private final UserStudentService studentService;
    private final UserTeacherService teacherService;
    private final UserDormitoryManagerService dormitoryManagerService;

    @Operation(summary = "获取学生分页列表")
    @GetMapping("/student/list")
    public PageResult<StudentPageVO> listStudent(@ParameterObject StudentPageQuery query) {
        Page<StudentPageVO> page = studentService.listStudent(query);
        return PageResult.success(page);
    }

    @Operation(summary = "获取教师分页列表")
    @GetMapping("/teacher/list")
    public PageResult<TeacherPageVO> listTeacher(@ParameterObject BaseQuery query) {
        Page<TeacherPageVO> page = teacherService.listTeacher(query);
        return PageResult.success(page);
    }

    @Operation(summary = "获取宿管分页列表")
    @GetMapping("/dm/list")
    public PageResult<DormitoryManagerPageVO> listDormitoryManager(@ParameterObject BaseQuery query) {
        Page<DormitoryManagerPageVO> page = dormitoryManagerService.listDormitoryManager(query);
        return PageResult.success(page);
    }

    @Operation(summary = "新增学生")
    @Log(value = "新增学生", module = LogModuleEnum.STUDENT)
    @RepeatSubmit
    @PostMapping("/student/add")
    public Result<Void> addStudent(@Validated @RequestBody UserStudentForm form) {
        boolean result = studentService.saveStudent(form);
        return Result.judge(result);
    }

    @Operation(summary = "新增教师")
    @Log(value = "新增教师", module = LogModuleEnum.TEACHER)
    @RepeatSubmit
    @PostMapping("/teacher/add")
    public Result<Void> addTeacher(@Validated @RequestBody UserTeacherForm form) {
        boolean result = teacherService.saveTeacher(form);
        return Result.judge(result);
    }

    @Operation(summary = "新增宿管")
    @Log(value = "新增宿管", module = LogModuleEnum.DORMITORY_MANAGER)
    @RepeatSubmit
    @PostMapping("/dm/add")
    public Result<Void> addDormitoryManager(@Validated @RequestBody UserDormitoryManagerForm form) {
        boolean result = dormitoryManagerService.saveDormitoryManager(form);
        return Result.judge(result);
    }

    @Operation(summary = "修改学生")
    @Log(value = "修改学生", module = LogModuleEnum.STUDENT)
    @RepeatSubmit
    @PostMapping("/student/edit/{id}")
    public Result<Void> editStudent(@Validated @RequestBody UserStudentForm form,
                                    @PathVariable String id) {
        form.setId(id);
        boolean result = studentService.saveStudent(form);
        return Result.judge(result);
    }

    @Operation(summary = "修改教师")
    @Log(value = "修改教师", module = LogModuleEnum.TEACHER)
    @RepeatSubmit
    @PostMapping("/teacher/edit/{id}")
    public Result<Void> editTeacher(@Validated @RequestBody UserTeacherForm form,
                                    @PathVariable String id) {
        form.setId(id);
        boolean result = teacherService.saveTeacher(form);
        return Result.judge(result);
    }

    @Operation(summary = "修改宿管")
    @Log(value = "修改宿管", module = LogModuleEnum.DORMITORY_MANAGER)
    @RepeatSubmit
    @PostMapping("/dm/edit/{id}")
    public Result<Void> editDormitoryManager(@Validated @RequestBody UserDormitoryManagerForm form,
                                             @PathVariable String id) {
        form.setId(id);
        boolean result = dormitoryManagerService.saveDormitoryManager(form);
        return Result.judge(result);
    }
}
