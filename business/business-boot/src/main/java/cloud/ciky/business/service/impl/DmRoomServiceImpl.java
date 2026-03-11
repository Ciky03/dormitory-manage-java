package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.DmRoom;
import cloud.ciky.business.mapper.DmRoomMapper;
import cloud.ciky.business.service.DmRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 宿舍表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-11 17:04:20
 */
@Slf4j
@Service
public class DmRoomServiceImpl extends ServiceImpl<DmRoomMapper, DmRoom> implements DmRoomService {

}
