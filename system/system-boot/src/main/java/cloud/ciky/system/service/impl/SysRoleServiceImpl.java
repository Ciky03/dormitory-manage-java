package cloud.ciky.system.service.impl;

import cloud.ciky.system.model.entity.SysRole;
import cloud.ciky.system.mapper.SysRoleMapper;
import cloud.ciky.system.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2025-12-16 15:56:01
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

}
