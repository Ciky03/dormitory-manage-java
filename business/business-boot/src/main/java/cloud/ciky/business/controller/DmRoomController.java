package cloud.ciky.business.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.ciky.business.service.DmRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 宿舍表 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2026-03-11 17:04:20
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/room")
public class DmRoomController {

    private final DmRoomService roomService;
}
