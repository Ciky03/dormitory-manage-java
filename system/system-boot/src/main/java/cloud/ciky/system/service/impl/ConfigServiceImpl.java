package cloud.ciky.system.service.impl;

import cloud.ciky.system.service.ConfigService;
import cloud.ciky.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * </p>
 *
 * @author ciky
 * @since 2026-01-02 18:44
 */
@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {

    private final SysMenuService menuService;

    @Override
    public Long getSort(String business, String parentId) {

        return switch (business) {
            case "menu" -> menuService.getSort(parentId);
            default -> 1L;
        };
    }
}
