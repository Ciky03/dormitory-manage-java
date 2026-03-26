package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.DmTodoComment;
import cloud.ciky.business.mapper.DmTodoCommentMapper;
import cloud.ciky.business.service.DmTodoCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 宿舍待办评论表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
@Slf4j
@Service
public class DmTodoCommentServiceImpl extends ServiceImpl<DmTodoCommentMapper, DmTodoComment> implements DmTodoCommentService {

}
