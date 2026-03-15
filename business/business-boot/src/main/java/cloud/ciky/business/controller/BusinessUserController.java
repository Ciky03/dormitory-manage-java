package cloud.ciky.business.controller;

import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.result.Result;
import cloud.ciky.business.model.form.UserDormitoryManagerForm;
import cloud.ciky.business.model.form.UserStudentForm;
import cloud.ciky.business.model.form.UserTeacherForm;
import cloud.ciky.business.service.UserDormitoryManagerService;
import cloud.ciky.business.service.UserTeacherService;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.core.annotation.RepeatSubmit;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.ciky.business.service.UserStudentService;
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

}
