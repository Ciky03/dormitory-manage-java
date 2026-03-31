package cloud.ciky.business.service.impl;

import cloud.ciky.base.enums.UserTypeEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.business.mapper.DmTodoCommentMapper;
import cloud.ciky.business.mapper.DmTodoMapper;
import cloud.ciky.business.model.entity.DmTodo;
import cloud.ciky.business.model.entity.DmTodoComment;
import cloud.ciky.business.model.form.DmTodoCommentForm;
import cloud.ciky.business.model.vo.DmTodoCommentVO;
import cloud.ciky.business.service.DmTodoCommentService;
import cloud.ciky.business.service.RoomStudentService;
import cloud.ciky.business.utils.UserInfoUtil;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 宿舍待办评论服务实现类
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
@Service
@RequiredArgsConstructor
public class DmTodoCommentServiceImpl extends ServiceImpl<DmTodoCommentMapper, DmTodoComment> implements DmTodoCommentService {

    private final RoomStudentService roomStudentService;
    private final DmTodoMapper dmTodoMapper;

    @Override
    public List<DmTodoCommentVO> listComment(String todoId) {
        requireCurrentRoomTodo(todoId, false);
        List<DmTodoCommentVO> commentList = this.baseMapper.listCommentByTodoId(todoId);
        return commentList == null ? Collections.emptyList() : commentList;
    }

    @Override
    public boolean addComment(DmTodoCommentForm form) {
        DmTodo todo = requireCurrentRoomTodo(form.getTodoId(), true);
        if (Boolean.TRUE.equals(todo.getDelflag())) {
            throw new BusinessException("已删除任务禁止评论");
        }
        DmTodoComment entity = new DmTodoComment();
        entity.setTodoId(form.getTodoId());
        entity.setCommenterStudentId(requireStudentId());
        entity.setContent(form.getContent());
        entity.setCreateBy(SecurityUtils.getUserId());
        entity.setDelflag(false);
        return this.save(entity);
    }

    private DmTodo requireCurrentRoomTodo(String todoId, boolean includeDeleted) {
        String roomId = requireCurrentRoomId();
        LambdaQueryWrapper<DmTodo> wrapper = new LambdaQueryWrapper<DmTodo>()
                .eq(DmTodo::getId, todoId)
                .eq(DmTodo::getRoomId, roomId);
        if (!includeDeleted) {
            wrapper.eq(DmTodo::getDelflag, false);
        }
        wrapper.last("limit 1");
        DmTodo todo = dmTodoMapper.selectOne(wrapper);
        if (todo == null) {
            throw new BusinessException("待办不存在或无权访问");
        }
        return todo;
    }

    private String requireStudentId() {
        if (!Objects.equals(SecurityUtils.getUserType(), UserTypeEnum.STUDENT.getValue())) {
            throw new BusinessException("当前用户不是学生");
        }
        return UserInfoUtil.getCurrentStudentId();
    }

    private String requireCurrentRoomId() {
        String roomId = roomStudentService.getSelectedRoomId(requireStudentId());
        if (CharSequenceUtil.isBlank(roomId)) {
            throw new BusinessException("当前登录用户未绑定宿舍");
        }
        return roomId;
    }
}
