package cloud.ciky.business.controller;

import cloud.ciky.business.service.DmSharedCostDetailService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.ciky.business.service.DmSharedCostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 宿舍费用公摊主表 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2026-03-31 12:04:45
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/dormitory/cost")
public class DmSharedCostController {

    private final DmSharedCostService sharedCostService;
    private final DmSharedCostDetailService sharedCostDetailService;
}
