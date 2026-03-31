package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.DmSharedCost;
import cloud.ciky.business.mapper.DmSharedCostMapper;
import cloud.ciky.business.service.DmSharedCostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 宿舍费用公摊主表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-31 12:04:45
 */
@Slf4j
@Service
public class DmSharedCostServiceImpl extends ServiceImpl<DmSharedCostMapper, DmSharedCost> implements DmSharedCostService {

}
