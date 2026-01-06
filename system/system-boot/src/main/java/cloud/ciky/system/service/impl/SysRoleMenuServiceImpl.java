package cloud.ciky.system.service.impl;

import cloud.ciky.system.model.entity.SysRoleMenu;
import cloud.ciky.system.mapper.SysRoleMenuMapper;
import cloud.ciky.system.service.SysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * 角色和菜单关联表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-01-04 18:12:09
 */
@Slf4j
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    @Override
    public List<String> listMenuIdsByRoleId(String roleId) {
        return this.baseMapper.selectMenuIdsByRoleId(roleId);
    }
}
