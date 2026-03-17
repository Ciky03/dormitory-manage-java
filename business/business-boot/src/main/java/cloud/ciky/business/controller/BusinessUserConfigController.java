package cloud.ciky.business.controller;

import cloud.ciky.business.service.ClassTeacherService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.ciky.business.service.ClassStudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
    private final ClassTeacherService classTeacherService;


}
