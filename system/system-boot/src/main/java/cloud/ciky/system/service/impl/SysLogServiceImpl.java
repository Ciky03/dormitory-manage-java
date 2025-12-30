package cloud.ciky.system.service.impl;

import cloud.ciky.system.model.entity.SysLog;
import cloud.ciky.system.mapper.SysLogMapper;
import cloud.ciky.system.service.SysLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 15:30:08
 */
@Slf4j
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

}
