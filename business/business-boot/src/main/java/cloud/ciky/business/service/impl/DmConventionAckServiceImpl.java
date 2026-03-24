package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.DmConventionAck;
import cloud.ciky.business.mapper.DmConventionAckMapper;
import cloud.ciky.business.service.DmConventionAckService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 宿舍公约阅读/同意记录表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-24 19:32:07
 */
@Slf4j
@Service
public class DmConventionAckServiceImpl extends ServiceImpl<DmConventionAckMapper, DmConventionAck> implements DmConventionAckService {

}
