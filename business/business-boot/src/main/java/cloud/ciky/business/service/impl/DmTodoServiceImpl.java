package cloud.ciky.business.service.impl;

import cloud.ciky.base.IBaseEnum;
import cloud.ciky.base.enums.UserTypeEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.base.model.Option;
import cloud.ciky.business.enums.DmTodoPriorityEnum;
import cloud.ciky.business.enums.DmTodoStatusEnum;
import cloud.ciky.business.mapper.DmTodoMapper;
import cloud.ciky.business.model.entity.DmTodo;
import cloud.ciky.business.model.form.DmTodoForm;
import cloud.ciky.business.model.query.DmTodoPageQuery;
import cloud.ciky.business.model.vo.DmTodoCommentVO;
import cloud.ciky.business.model.vo.DmTodoDetailVO;
import cloud.ciky.business.model.vo.DmTodoPageVO;
import cloud.ciky.business.model.vo.DmTodoStatVO;
import cloud.ciky.business.service.DmTodoCommentService;
import cloud.ciky.business.service.DmTodoService;
import cloud.ciky.business.service.RoomStudentService;
import cloud.ciky.business.utils.UserInfoUtil;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Todo service implementation.
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
@Service
@RequiredArgsConstructor
public class DmTodoServiceImpl extends ServiceImpl<DmTodoMapper, DmTodo> implements DmTodoService {

    private static final String MSG_NOT_STUDENT = "\u5f53\u524d\u7528\u6237\u4e0d\u662f\u5b66\u751f";
    private static final String MSG_ROOM_NOT_BOUND = "\u5f53\u524d\u767b\u5f55\u7528\u6237\u672a\u7ed1\u5b9a\u5bbf\u820d";
    private static final String MSG_TODO_NOT_FOUND = "\u5f85\u529e\u4e0d\u5b58\u5728\u6216\u65e0\u6743\u8bbf\u95ee";
    private static final String MSG_EDIT_FORBIDDEN = "\u4ec5\u521b\u5efa\u4eba\u6216\u8d1f\u8d23\u4eba\u53ef\u7f16\u8f91\u8be5\u5f85\u529e";
    private static final String MSG_EDIT_STATUS_FORBIDDEN = "\u5df2\u5b8c\u6210\u6216\u5df2\u53d6\u6d88\u5f85\u529e\u4e0d\u5141\u8bb8\u7f16\u8f91";
    private static final String MSG_DELETE_FORBIDDEN = "\u4ec5\u521b\u5efa\u4eba\u6216\u8d1f\u8d23\u4eba\u53ef\u5220\u9664\u8be5\u5f85\u529e";
    private static final String MSG_DELETE_STATUS_FORBIDDEN = "\u5df2\u5b8c\u6210\u6216\u5df2\u53d6\u6d88\u5f85\u529e\u4e0d\u5141\u8bb8\u5220\u9664";
    private static final String MSG_START_STATUS_FORBIDDEN = "\u4ec5\u5f85\u5904\u7406\u72b6\u6001\u53ef\u5f00\u59cb\u5904\u7406";
    private static final String MSG_COMPLETE_FORBIDDEN = "\u4ec5\u521b\u5efa\u4eba\u6216\u8d1f\u8d23\u4eba\u53ef\u5b8c\u6210\u8be5\u5f85\u529e";
    private static final String MSG_COMPLETE_STATUS_FORBIDDEN = "\u4ec5\u5f85\u5904\u7406\u6216\u8fdb\u884c\u4e2d\u72b6\u6001\u53ef\u5b8c\u6210";
    private static final String MSG_CANCEL_REASON_REQUIRED = "\u53d6\u6d88\u539f\u56e0\u4e0d\u80fd\u4e3a\u7a7a";
    private static final String MSG_CANCEL_FORBIDDEN = "\u4ec5\u521b\u5efa\u4eba\u6216\u8d1f\u8d23\u4eba\u53ef\u53d6\u6d88\u8be5\u5f85\u529e";
    private static final String MSG_CANCEL_STATUS_FORBIDDEN = "\u4ec5\u5f85\u5904\u7406\u6216\u8fdb\u884c\u4e2d\u72b6\u6001\u53ef\u53d6\u6d88";
    private static final String MSG_ASSIGNEE_INVALID = "\u8d1f\u8d23\u4eba\u5fc5\u987b\u662f\u5f53\u524d\u5bbf\u820d\u6210\u5458";

    private final RoomStudentService roomStudentService;
    private final DmTodoCommentService dmTodoCommentService;

    @Override
    public DmTodoStatVO getTodoStat() {
        String roomId = requireCurrentRoomId();
        LocalDateTime weekStart = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime weekEnd = weekStart.plusDays(7);
        DmTodoStatVO statVO = this.baseMapper.selectTodoStat(roomId, weekStart, weekEnd);
        if (statVO == null) {
            statVO = new DmTodoStatVO();
            statVO.setRoomId(roomId);
        }
        statVO.setTotalCount(defaultZero(statVO.getTotalCount()));
        statVO.setPendingCount(defaultZero(statVO.getPendingCount()));
        statVO.setProcessingCount(defaultZero(statVO.getProcessingCount()));
        statVO.setWeekCompletedCount(defaultZero(statVO.getWeekCompletedCount()));
        return statVO;
    }

    @Override
    public Page<DmTodoPageVO> listTodo(DmTodoPageQuery query) {
        String roomId = requireCurrentRoomId();
        Page<DmTodoPageVO> page = this.baseMapper.selectTodoPage(
                new Page<>(query.getPageNum(), query.getPageSize()),
                query,
                roomId,
                LocalDateTime.now()
        );
        if (page == null) {
            return new Page<>(query.getPageNum(), query.getPageSize());
        }
        if (page.getRecords() != null) {
            page.getRecords().forEach(this::fillPageLabels);
        }
        return page;
    }

    @Override
    public DmTodoDetailVO getTodoDetail(String id) {
        String studentId = requireStudentId();
        String roomId = requireCurrentRoomId();
        DmTodoDetailVO detailVO = this.baseMapper.selectTodoDetail(id, roomId);
        if (detailVO == null) {
            throw new BusinessException(MSG_TODO_NOT_FOUND);
        }
        fillDetailLabels(detailVO);
        detailVO.setCanEdit(canEdit(detailVO, studentId));
        detailVO.setCanStart(canStart(detailVO));
        detailVO.setCanComplete(canOperate(detailVO, studentId));
        detailVO.setCanCancel(canOperate(detailVO, studentId));
        List<DmTodoCommentVO> commentList = dmTodoCommentService.listComment(id);
        detailVO.setCommentList(commentList == null ? Collections.emptyList() : commentList);
        return detailVO;
    }

    @Override
    public boolean saveTodo(DmTodoForm form) {
        String studentId = requireStudentId();
        String roomId = requireCurrentRoomId();
        String assigneeStudentId = normalizeNullableString(form.getAssigneeStudentId());
        validateAssignee(roomId, assigneeStudentId);

        if (CharSequenceUtil.isNotBlank(form.getId())) {
            DmTodo current = requireCurrentRoomTodo(form.getId(), roomId);
            if (!isOwner(current.getCreatorStudentId(), current.getAssigneeStudentId(), studentId)) {
                throw new BusinessException(MSG_EDIT_FORBIDDEN);
            }
            if (Objects.equals(current.getStatus(), 2) || Objects.equals(current.getStatus(), 3)) {
                throw new BusinessException(MSG_EDIT_STATUS_FORBIDDEN);
            }
            return this.lambdaUpdate()
                    .eq(DmTodo::getId, current.getId())
                    .eq(DmTodo::getRoomId, roomId)
                    .eq(DmTodo::getDelflag, false)
                    .set(DmTodo::getTitle, form.getTitle())
                    .set(DmTodo::getContent, form.getContent())
                    .set(DmTodo::getPriority, form.getPriority())
                    .set(DmTodo::getAssigneeStudentId, assigneeStudentId)
                    .set(DmTodo::getDueTime, form.getDueTime())
                    .set(DmTodo::getUpdateBy, SecurityUtils.getUserId())
                    .update();
        }

        DmTodo entity = new DmTodo();
        entity.setRoomId(roomId);
        entity.setCreatorStudentId(studentId);
        entity.setTitle(form.getTitle());
        entity.setContent(form.getContent());
        entity.setPriority(form.getPriority());
        entity.setStatus(0);
        entity.setAssigneeStudentId(assigneeStudentId);
        entity.setDueTime(form.getDueTime());
        entity.setCreateBy(SecurityUtils.getUserId());
        entity.setDelflag(false);
        return this.save(entity);
    }

    @Override
    public boolean deleteTodo(String id) {
        String studentId = requireStudentId();
        DmTodo current = requireCurrentRoomTodo(id, requireCurrentRoomId());
        if (!isOwner(current.getCreatorStudentId(), current.getAssigneeStudentId(), studentId)) {
            throw new BusinessException(MSG_DELETE_FORBIDDEN);
        }
        if (Objects.equals(current.getStatus(), 2) || Objects.equals(current.getStatus(), 3)) {
            throw new BusinessException(MSG_DELETE_STATUS_FORBIDDEN);
        }
        current.setDelflag(true);
        current.setUpdateBy(SecurityUtils.getUserId());
        return this.updateById(current);
    }

    @Override
    public boolean startTodo(String id) {
        String studentId = requireStudentId();
        DmTodo current = requireCurrentRoomTodo(id, requireCurrentRoomId());
        if (!Objects.equals(current.getStatus(), 0)) {
            throw new BusinessException(MSG_START_STATUS_FORBIDDEN);
        }
        current.setStatus(1);
        current.setStartTime(LocalDateTime.now());
        if (CharSequenceUtil.isBlank(current.getAssigneeStudentId())) {
            current.setAssigneeStudentId(studentId);
        }
        current.setUpdateBy(SecurityUtils.getUserId());
        return this.updateById(current);
    }

    @Override
    public boolean completeTodo(String id) {
        String studentId = requireStudentId();
        DmTodo current = requireCurrentRoomTodo(id, requireCurrentRoomId());
        if (!canOperate(current, studentId)) {
            throw new BusinessException(MSG_COMPLETE_FORBIDDEN);
        }
        if (!Objects.equals(current.getStatus(), 0) && !Objects.equals(current.getStatus(), 1)) {
            throw new BusinessException(MSG_COMPLETE_STATUS_FORBIDDEN);
        }
        current.setStatus(2);
        current.setCompletedTime(LocalDateTime.now());
        current.setCompletedBy(studentId);
        current.setUpdateBy(SecurityUtils.getUserId());
        return this.updateById(current);
    }

    @Override
    public boolean cancelTodo(String id, String cancelReason) {
        String reason = normalizeNullableString(cancelReason);
        if (CharSequenceUtil.isBlank(reason)) {
            throw new BusinessException(MSG_CANCEL_REASON_REQUIRED);
        }
        String studentId = requireStudentId();
        DmTodo current = requireCurrentRoomTodo(id, requireCurrentRoomId());
        if (!canOperate(current, studentId)) {
            throw new BusinessException(MSG_CANCEL_FORBIDDEN);
        }
        if (!Objects.equals(current.getStatus(), 0) && !Objects.equals(current.getStatus(), 1)) {
            throw new BusinessException(MSG_CANCEL_STATUS_FORBIDDEN);
        }
        current.setStatus(3);
        current.setCancelReason(reason);
        current.setUpdateBy(SecurityUtils.getUserId());
        return this.updateById(current);
    }

    @Override
    public List<Option<String>> listAssigneeOptions() {
        return this.baseMapper.listAssigneeOptions(requireCurrentRoomId());
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

    private boolean canEdit(DmTodoDetailVO detailVO, String studentId) {
        return isOwner(detailVO.getCreatorStudentId(), detailVO.getAssigneeStudentId(), studentId)
                && !Objects.equals(detailVO.getStatus(), 2)
                && !Objects.equals(detailVO.getStatus(), 3);
    }

    private boolean canStart(DmTodoDetailVO detailVO) {
        return Objects.equals(detailVO.getStatus(), 0);
    }

    private boolean canOperate(DmTodoDetailVO detailVO, String studentId) {
        return isOwner(detailVO.getCreatorStudentId(), detailVO.getAssigneeStudentId(), studentId)
                && (Objects.equals(detailVO.getStatus(), 0) || Objects.equals(detailVO.getStatus(), 1));
    }

    private void validateAssignee(String roomId, String assigneeStudentId) {
        if (CharSequenceUtil.isBlank(assigneeStudentId)) {
            return;
        }
        Integer count = this.baseMapper.countCurrentRoomStudent(roomId, assigneeStudentId);
        if (count == null || count == 0) {
            throw new BusinessException(MSG_ASSIGNEE_INVALID);
        }
    }

    private DmTodo requireCurrentRoomTodo(String id, String roomId) {
        DmTodo entity = this.getOne(new LambdaQueryWrapper<DmTodo>()
                .eq(DmTodo::getId, id)
                .eq(DmTodo::getRoomId, roomId)
                .eq(DmTodo::getDelflag, false)
                .last("limit 1"));
        if (entity == null) {
            throw new BusinessException(MSG_TODO_NOT_FOUND);
        }
        return entity;
    }

    private boolean canOperate(DmTodo entity, String studentId) {
        return isOwner(entity.getCreatorStudentId(), entity.getAssigneeStudentId(), studentId);
    }

    private void fillPageLabels(DmTodoPageVO pageVO) {
        pageVO.setPriorityLabel(IBaseEnum.getLabelByValue(pageVO.getPriority(), DmTodoPriorityEnum.class));
        pageVO.setStatusLabel(IBaseEnum.getLabelByValue(pageVO.getStatus(), DmTodoStatusEnum.class));
    }

    private void fillDetailLabels(DmTodoDetailVO detailVO) {
        detailVO.setPriorityLabel(IBaseEnum.getLabelByValue(detailVO.getPriority(), DmTodoPriorityEnum.class));
        detailVO.setStatusLabel(IBaseEnum.getLabelByValue(detailVO.getStatus(), DmTodoStatusEnum.class));
    }

    private boolean isOwner(String creatorStudentId, String assigneeStudentId, String studentId) {
        return Objects.equals(creatorStudentId, studentId) || Objects.equals(assigneeStudentId, studentId);
    }

    private Integer defaultZero(Integer value) {
        return value == null ? 0 : value;
    }

    private String normalizeNullableString(String value) {
        String trimmed = CharSequenceUtil.trim(value);
        return CharSequenceUtil.isBlank(trimmed) ? null : trimmed;
    }
}
