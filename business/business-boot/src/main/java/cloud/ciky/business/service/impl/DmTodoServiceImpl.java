package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.DmTodo;
import cloud.ciky.business.mapper.DmTodoMapper;
import cloud.ciky.business.service.DmTodoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 宿舍待办主表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
@Slf4j
@Service
public class DmTodoServiceImpl extends ServiceImpl<DmTodoMapper, DmTodo> implements DmTodoService {

}
