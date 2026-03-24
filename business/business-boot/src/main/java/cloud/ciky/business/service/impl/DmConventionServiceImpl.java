package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.DmConvention;
import cloud.ciky.business.mapper.DmConventionMapper;
import cloud.ciky.business.service.DmConventionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 宿舍公约版本表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-24 19:32:06
 */
@Slf4j
@Service
public class DmConventionServiceImpl extends ServiceImpl<DmConventionMapper, DmConvention> implements DmConventionService {

}
