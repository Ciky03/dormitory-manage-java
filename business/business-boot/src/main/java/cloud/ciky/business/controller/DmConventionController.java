package cloud.ciky.business.controller;

import cloud.ciky.business.service.DmConventionAckService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.ciky.business.service.DmConventionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@RequestMapping("/convention")
public class DmConventionController {

    private final DmConventionService conventionService;
    private final DmConventionAckService conventionAckService;
}
