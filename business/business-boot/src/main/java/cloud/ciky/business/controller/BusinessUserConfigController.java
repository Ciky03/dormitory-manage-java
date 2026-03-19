package cloud.ciky.business.controller;

import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.result.Result;
import cloud.ciky.business.model.form.ClassStudentForm;
import cloud.ciky.business.model.form.ClassTeacherForm;
import cloud.ciky.business.service.BuildingDmService;
import cloud.ciky.business.service.ClassTeacherService;
import cloud.ciky.business.service.ClassStudentService;
import cloud.ciky.business.service.RoomStudentService;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.core.annotation.RepeatSubmit;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.RSASSAPSSparams;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 人员配置 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2026-03-17 11:25:44
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/person/config")
public class BusinessUserConfigController {

    private final ClassStudentService classStudentService;
    private final BuildingDmService buildingDmService;
    private final RoomStudentService roomStudentService;

    @Operation(summary = "保存班级学生信息")
    @Log(value = "保存班级学生信息", module = LogModuleEnum.STUDENT)
    @RepeatSubmit
    @PostMapping("/class/student/add")
    public Result<Void> saveClassStudent(@Validated @RequestBody ClassStudentForm form) {
        boolean result = classStudentService.saveClassStudent(form);
        return Result.judge(result);
    }

}
