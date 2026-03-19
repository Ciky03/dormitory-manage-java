package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.RoomStudent;
import cloud.ciky.business.mapper.RoomStudentMapper;
import cloud.ciky.business.service.RoomStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 学生宿舍关联表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-19 16:43:50
 */
@Slf4j
@Service
public class RoomStudentServiceImpl extends ServiceImpl<RoomStudentMapper, RoomStudent> implements RoomStudentService {

}
