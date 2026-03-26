package cloud.ciky.business.controller;

import cloud.ciky.business.service.DmTodoCommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.ciky.business.service.DmTodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 宿舍待办主表 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/dormitory/todo")
public class DmTodoController {

    private final DmTodoService dmTodoService;
    private final DmTodoCommentService dmTodoCommentService;
}
