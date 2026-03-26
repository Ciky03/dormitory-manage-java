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
 * Todo comment service implementation.
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
@Service
@RequiredArgsConstructor
public class DmTodoCommentServiceImpl extends ServiceImpl<DmTodoCommentMapper, DmTodoComment> implements DmTodoCommentService {

    private static final String MSG_NOT_STUDENT = "\u5f53\u524d\u7528\u6237\u4e0d\u662f\u5b66\u751f";
    private static final String MSG_ROOM_NOT_BOUND = "\u5f53\u524d\u767b\u5f55\u7528\u6237\u672a\u7ed1\u5b9a\u5bbf\u820d";
    private static final String MSG_TODO_NOT_FOUND = "\u5f85\u529e\u4e0d\u5b58\u5728\u6216\u65e0\u6743\u8bbf\u95ee";
    private static final String MSG_DELETED_TODO_COMMENT_FORBIDDEN = "\u5df2\u5220\u9664\u4efb\u52a1\u7981\u6b62\u8bc4\u8bba";

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
            throw new BusinessException(MSG_DELETED_TODO_COMMENT_FORBIDDEN);
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
            throw new BusinessException(MSG_TODO_NOT_FOUND);
        }
        return todo;
    }

    private String requireStudentId() {
        if (!Objects.equals(SecurityUtils.getUserType(), UserTypeEnum.STUDENT.getValue())) {
            throw new BusinessException(MSG_NOT_STUDENT);
        }
        return UserInfoUtil.getCurrentStudentId();
    }

    private String requireCurrentRoomId() {
        String roomId = roomStudentService.getSelectedRoomId(requireStudentId());
        if (CharSequenceUtil.isBlank(roomId)) {
            throw new BusinessException(MSG_ROOM_NOT_BOUND);
        }
        return roomId;
    }
}
