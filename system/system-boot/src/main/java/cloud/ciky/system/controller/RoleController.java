package cloud.ciky.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cloud.ciky.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2025-12-16 15:56:01
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/role")
public class RoleController {

    private final SysRoleService roleService;
}
