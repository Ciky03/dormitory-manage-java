package cloud.ciky.business.service.impl;

import cloud.ciky.base.constant.JwtClaimConstants;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.base.model.Option;
import cloud.ciky.business.mapper.DmTodoMapper;
import cloud.ciky.business.model.entity.DmTodo;
import cloud.ciky.business.model.form.DmTodoForm;
import cloud.ciky.business.model.query.DmTodoPageQuery;
import cloud.ciky.business.model.vo.DmTodoCommentVO;
import cloud.ciky.business.model.vo.DmTodoDetailVO;
import cloud.ciky.business.model.vo.DmTodoStatVO;
import cloud.ciky.business.service.DmTodoCommentService;
import cloud.ciky.business.service.RoomStudentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DmTodoServiceImplTest {

    @Mock
    private DmTodoMapper dmTodoMapper;

    @Mock
    private RoomStudentService roomStudentService;

    @Mock
    private DmTodoCommentService dmTodoCommentService;

    @InjectMocks
    private DmTodoServiceImpl service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "baseMapper", dmTodoMapper);
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claims(claims -> claims.putAll(Map.of(
                        JwtClaimConstants.USER_ID, "sys-1",
                        JwtClaimConstants.BUSINESS_USER_ID, "stu-1",
                        JwtClaimConstants.USER_TYPE, 1
                )))
                .build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldPopulateDetailPermissionsAndCommentList() {
        DmTodoDetailVO detail = new DmTodoDetailVO();
        detail.setId("todo-1");
        detail.setStatus(1);
        detail.setCreatorStudentId("stu-1");
        detail.setAssigneeStudentId("stu-2");

        when(roomStudentService.getSelectedRoomId("stu-1")).thenReturn("room-101");
        when(dmTodoMapper.selectTodoDetail("todo-1", "room-101")).thenReturn(detail);
        when(dmTodoCommentService.listComment("todo-1")).thenReturn(List.of(new DmTodoCommentVO()));

        DmTodoDetailVO result = service.getTodoDetail("todo-1");

        assertTrue(Boolean.TRUE.equals(result.getCanEdit()));
        assertTrue(Boolean.TRUE.equals(result.getCanComplete()));
        assertTrue(Boolean.TRUE.equals(result.getCanCancel()));
        assertEquals(1, result.getCommentList().size());
    }

    @Test
    void shouldForwardListQueryWithCurrentRoomAndNow() {
        when(roomStudentService.getSelectedRoomId("stu-1")).thenReturn("room-101");
        when(dmTodoMapper.selectTodoPage(any(), any(), eq("room-101"), any(LocalDateTime.class)))
                .thenReturn(new Page<>(1, 10, 0));

        DmTodoPageQuery query = new DmTodoPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setState(2);

        service.listTodo(query);

        verify(dmTodoMapper).selectTodoPage(any(), eq(query), eq("room-101"), any(LocalDateTime.class));
    }

    @Test
    void shouldReturnStatAndAssigneeOptionsForCurrentRoom() {
        DmTodoStatVO statVO = new DmTodoStatVO();
        statVO.setRoomId("room-101");
        when(roomStudentService.getSelectedRoomId("stu-1")).thenReturn("room-101");
        when(dmTodoMapper.selectTodoStat(eq("room-101"), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(statVO);
        when(dmTodoMapper.listAssigneeOptions("room-101")).thenReturn(List.of(new Option<>("stu-2", "\u674e\u56db")));

        assertEquals("room-101", service.getTodoStat().getRoomId());
        assertEquals("\u674e\u56db", service.listAssigneeOptions().get(0).getLabel());
    }

    @Test
    void shouldCreatePendingTodoInCurrentRoom() {
        when(roomStudentService.getSelectedRoomId("stu-1")).thenReturn("room-101");
        when(dmTodoMapper.countCurrentRoomStudent("room-101", "stu-2")).thenReturn(1);

        DmTodoForm form = new DmTodoForm();
        form.setTitle("Check door lock");
        form.setContent("Check door lock before sleep");
        form.setPriority(2);
        form.setAssigneeStudentId("stu-2");

        DmTodoServiceImpl spyService = spy(service);
        doReturn(true).when(spyService).save(any(DmTodo.class));

        assertTrue(spyService.saveTodo(form));
        ArgumentCaptor<DmTodo> captor = ArgumentCaptor.forClass(DmTodo.class);
        verify(spyService).save(captor.capture());
        assertEquals("room-101", captor.getValue().getRoomId());
        assertEquals("stu-1", captor.getValue().getCreatorStudentId());
        assertEquals(0, captor.getValue().getStatus());
        assertEquals("stu-2", captor.getValue().getAssigneeStudentId());
    }

    @Test
    void shouldRejectEditingCompletedTodo() {
        when(roomStudentService.getSelectedRoomId("stu-1")).thenReturn("room-101");
        DmTodoServiceImpl spyService = spy(service);
        DmTodo entity = new DmTodo();
        entity.setId("todo-1");
        entity.setRoomId("room-101");
        entity.setCreatorStudentId("stu-1");
        entity.setStatus(2);
        entity.setDelflag(false);
        doReturn(entity).when(spyService).getOne(any(LambdaQueryWrapper.class));

        DmTodoForm form = new DmTodoForm();
        form.setId("todo-1");
        form.setTitle("Updated title");
        form.setContent("Updated content");
        form.setPriority(1);

        assertThrows(BusinessException.class, () -> spyService.saveTodo(form));
    }

    @Test
    void shouldAutoClaimTodoOnStart() {
        when(roomStudentService.getSelectedRoomId("stu-1")).thenReturn("room-101");
        DmTodoServiceImpl spyService = spy(service);
        DmTodo entity = new DmTodo();
        entity.setId("todo-1");
        entity.setRoomId("room-101");
        entity.setStatus(0);
        entity.setCreatorStudentId("stu-2");
        entity.setDelflag(false);
        doReturn(entity).when(spyService).getOne(any(LambdaQueryWrapper.class));
        doReturn(true).when(spyService).updateById(any(DmTodo.class));

        assertTrue(spyService.startTodo("todo-1"));
        ArgumentCaptor<DmTodo> captor = ArgumentCaptor.forClass(DmTodo.class);
        verify(spyService).updateById(captor.capture());
        assertEquals(1, captor.getValue().getStatus());
        assertEquals("stu-1", captor.getValue().getAssigneeStudentId());
    }

    @Test
    void shouldRequireCancelReason() {
        when(roomStudentService.getSelectedRoomId("stu-1")).thenReturn("room-101");

        assertThrows(BusinessException.class, () -> service.cancelTodo("todo-1", ""));
    }
}
