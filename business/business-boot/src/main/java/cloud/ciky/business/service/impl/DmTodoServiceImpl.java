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
import cn.hutool.core.util.ObjUtil;
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
 * 宿舍待办服务实现类
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
@Service
@RequiredArgsConstructor
public class DmTodoServiceImpl extends ServiceImpl<DmTodoMapper, DmTodo> implements DmTodoService {


    private final RoomStudentService roomStudentService;
    private final DmTodoCommentService dmTodoCommentService;

    @Override
    public DmTodoStatVO getTodoStat() {
        String studentId = UserInfoUtil.getCurrentStudentId();
        LocalDateTime weekStart = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime weekEnd = weekStart.plusDays(7);
        DmTodoStatVO statVO = this.baseMapper.selectTodoStat(studentId, weekStart, weekEnd);
        if (ObjUtil.isNotNull(statVO)) {
            statVO.setTotalCount(defaultZero(statVO.getTotalCount()));
            statVO.setPendingCount(defaultZero(statVO.getPendingCount()));
            statVO.setProcessingCount(defaultZero(statVO.getProcessingCount()));
            statVO.setWeekCompletedCount(defaultZero(statVO.getWeekCompletedCount()));
        }
        return statVO;
    }

    @Override
    public Page<DmTodoPageVO> listTodo(DmTodoPageQuery query) {
        query.setRoomId(roomStudentService.getSelectedRoomIdThrowExp(UserInfoUtil.getCurrentStudentId()));
        query.setNow(LocalDateTime.now());
        Page<DmTodoPageVO> page = this.baseMapper.selectTodoPage(new Page<>(query.getPageNum(), query.getPageSize()), query);
        if (page.getRecords() != null) {
            page.getRecords().forEach(this::fillPageLabels);
        }
        return page;
    }

    @Override
    public DmTodoDetailVO getTodoDetail(String id) {
        String studentId = UserInfoUtil.getCurrentStudentId();
        String roomId = roomStudentService.getSelectedRoomIdThrowExp(studentId);
        DmTodoDetailVO detailVO = this.baseMapper.selectTodoDetail(id, roomId);
        if (detailVO == null) {
            throw new BusinessException("待办不存在或无权访问");
        }
        detailVO.setPriorityLabel(IBaseEnum.getLabelByValue(detailVO.getPriority(), DmTodoPriorityEnum.class));
        detailVO.setStatusLabel(IBaseEnum.getLabelByValue(detailVO.getStatus(), DmTodoStatusEnum.class));
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
        String studentId = UserInfoUtil.getCurrentStudentId();
        String roomId = roomStudentService.getSelectedRoomIdThrowExp(studentId);
        String assigneeStudentId = normalizeNullableString(form.getAssigneeStudentId());
        validateAssignee(roomId, assigneeStudentId);

        String id = form.getId();
        DmTodo entity = new DmTodo();
        if (CharSequenceUtil.isNotBlank(id)) {
            //编辑
            DmTodo current = requireCurrentRoomTodo(id, roomId);
            if (!isOwner(current.getCreatorStudentId(), current.getAssigneeStudentId(), studentId)) {
                throw new BusinessException("仅创建人或负责人可编辑该待办");
            }
            if (Objects.equals(current.getStatus(), DmTodoStatusEnum.COMPLETED.getValue())
                    || Objects.equals(current.getStatus(), DmTodoStatusEnum.CANCELED.getValue())) {
                throw new BusinessException("已完成或已取消待办不允许编辑");
            }
            entity.setId(id);
            entity.setUpdateBy(SecurityUtils.getUserId());
            entity.setCreatorStudentId(current.getCreatorStudentId());
        } else {
            //新增
            entity.setCreateBy(SecurityUtils.getUserId());
            entity.setCreatorStudentId(studentId);
        }
        entity.setRoomId(roomId);
        entity.setTitle(form.getTitle());
        entity.setContent(form.getContent());
        entity.setPriority(form.getPriority());
        entity.setStatus(DmTodoStatusEnum.PENDING.getValue());
        entity.setAssigneeStudentId(assigneeStudentId);
        entity.setDueTime(form.getDueTime());
        return this.saveOrUpdate(entity);
    }

    @Override
    public boolean deleteTodo(String id) {
        String studentId = UserInfoUtil.getCurrentStudentId();
        DmTodo current = requireCurrentRoomTodo(id, roomStudentService.getSelectedRoomIdThrowExp(studentId));
        if (!isOwner(current.getCreatorStudentId(), current.getAssigneeStudentId(), studentId)) {
            throw new BusinessException("仅创建人或负责人可删除该待办");
        }
        if (Objects.equals(current.getStatus(), DmTodoStatusEnum.COMPLETED.getValue())
                || Objects.equals(current.getStatus(), DmTodoStatusEnum.CANCELED.getValue())) {
            throw new BusinessException("已完成或已取消待办不允许删除");
        }
        current.setDelflag(true);
        current.setUpdateBy(SecurityUtils.getUserId());
        return this.updateById(current);
    }

    @Override
    public boolean startTodo(String id) {
        String studentId = UserInfoUtil.getCurrentStudentId();
        DmTodo current = requireCurrentRoomTodo(id, roomStudentService.getSelectedRoomIdThrowExp(studentId));
        if (!Objects.equals(current.getStatus(), DmTodoStatusEnum.PENDING.getValue())) {
            throw new BusinessException("仅待处理状态可开始处理");
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
        String studentId = UserInfoUtil.getCurrentStudentId();
        DmTodo current = requireCurrentRoomTodo(id, roomStudentService.getSelectedRoomIdThrowExp(studentId));
        if (!canOperate(current, studentId)) {
            throw new BusinessException("仅创建人或负责人可完成该待办");
        }
        if (!Objects.equals(current.getStatus(), DmTodoStatusEnum.PENDING.getValue())
                && !Objects.equals(current.getStatus(), DmTodoStatusEnum.PROCESSING.getValue())) {
            throw new BusinessException("仅待处理或进行中状态可完成");
        }
        current.setStatus(DmTodoStatusEnum.COMPLETED.getValue());
        current.setCompletedTime(LocalDateTime.now());
        current.setCompletedBy(studentId);
        current.setUpdateBy(SecurityUtils.getUserId());
        return this.updateById(current);
    }

    @Override
    public boolean cancelTodo(String id, String cancelReason) {
        String reason = normalizeNullableString(cancelReason);
        if (CharSequenceUtil.isBlank(reason)) {
            throw new BusinessException("取消原因不能为空");
        }
        String studentId = UserInfoUtil.getCurrentStudentId();
        DmTodo current = requireCurrentRoomTodo(id, roomStudentService.getSelectedRoomIdThrowExp(studentId));
        if (!canOperate(current, studentId)) {
            throw new BusinessException("仅创建人或负责人可取消该待办");
        }
        if (!Objects.equals(current.getStatus(), DmTodoStatusEnum.PENDING.getValue())
                && !Objects.equals(current.getStatus(), DmTodoStatusEnum.PROCESSING.getValue())) {
            throw new BusinessException("仅待处理或进行中状态可取消");
        }
        current.setStatus(DmTodoStatusEnum.CANCELED.getValue());
        current.setCancelReason(reason);
        current.setUpdateBy(SecurityUtils.getUserId());
        return this.updateById(current);
    }

    @Override
    public List<Option<String>> listAssigneeOptions() {
        return this.baseMapper.listAssigneeOptions(roomStudentService.getSelectedRoomIdThrowExp(UserInfoUtil.getCurrentStudentId()));
    }

    private boolean canEdit(DmTodoDetailVO detailVO, String studentId) {
        return isOwner(detailVO.getCreatorStudentId(), detailVO.getAssigneeStudentId(), studentId)
                && !Objects.equals(detailVO.getStatus(), DmTodoStatusEnum.COMPLETED.getValue())
                && !Objects.equals(detailVO.getStatus(), DmTodoStatusEnum.CANCELED.getValue());
    }

    private boolean canStart(DmTodoDetailVO detailVO) {
        return Objects.equals(detailVO.getStatus(), DmTodoStatusEnum.PENDING.getValue());
    }

    private boolean canOperate(DmTodoDetailVO detailVO, String studentId) {
        return isOwner(detailVO.getCreatorStudentId(), detailVO.getAssigneeStudentId(), studentId)
                && (Objects.equals(detailVO.getStatus(), DmTodoStatusEnum.PENDING.getValue()) || Objects.equals(detailVO.getStatus(), DmTodoStatusEnum.PROCESSING.getValue()));
    }

    private void validateAssignee(String roomId, String assigneeStudentId) {
        if (CharSequenceUtil.isBlank(assigneeStudentId)) {
            return;
        }
        Integer count = this.baseMapper.countCurrentRoomStudent(roomId, assigneeStudentId);
        if (count == null || count == 0) {
            throw new BusinessException("负责人必须是当前宿舍成员");
        }
    }

    private DmTodo requireCurrentRoomTodo(String id, String roomId) {
        DmTodo entity = this.getOne(new LambdaQueryWrapper<DmTodo>()
                .eq(DmTodo::getId, id)
                .eq(DmTodo::getRoomId, roomId)
                .eq(DmTodo::getDelflag, false)
                .last("limit 1"));
        if (entity == null) {
            throw new BusinessException("待办不存在或无权访问");
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
