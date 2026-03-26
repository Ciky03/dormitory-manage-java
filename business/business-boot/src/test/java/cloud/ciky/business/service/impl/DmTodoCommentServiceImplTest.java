package cloud.ciky.business.service.impl;

import cloud.ciky.base.constant.JwtClaimConstants;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.business.mapper.DmTodoCommentMapper;
import cloud.ciky.business.mapper.DmTodoMapper;
import cloud.ciky.business.model.entity.DmTodo;
import cloud.ciky.business.model.entity.DmTodoComment;
import cloud.ciky.business.model.form.DmTodoCommentForm;
import cloud.ciky.business.model.vo.DmTodoCommentVO;
import cloud.ciky.business.service.RoomStudentService;
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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DmTodoCommentServiceImplTest {

    @Mock
    private DmTodoCommentMapper dmTodoCommentMapper;

    @Mock
    private DmTodoMapper dmTodoMapper;

    @Mock
    private RoomStudentService roomStudentService;

    @InjectMocks
    private DmTodoCommentServiceImpl service;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(service, "baseMapper", dmTodoCommentMapper);
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
    void shouldReturnOrderedCommentListForAccessibleTodo() {
        DmTodo todo = new DmTodo();
        todo.setId("todo-1");
        todo.setRoomId("room-101");
        todo.setDelflag(false);
        when(roomStudentService.getSelectedRoomId("stu-1")).thenReturn("room-101");
        when(dmTodoMapper.selectOne(any())).thenReturn(todo);
        when(dmTodoCommentMapper.listCommentByTodoId("todo-1")).thenReturn(List.of(new DmTodoCommentVO()));

        assertEquals(1, service.listComment("todo-1").size());
        verify(dmTodoCommentMapper).listCommentByTodoId("todo-1");
    }

    @Test
    void shouldRejectCommentOnDeletedTodo() {
        DmTodo todo = new DmTodo();
        todo.setId("todo-1");
        todo.setRoomId("room-101");
        todo.setDelflag(true);
        when(roomStudentService.getSelectedRoomId("stu-1")).thenReturn("room-101");
        when(dmTodoMapper.selectOne(any())).thenReturn(todo);

        DmTodoCommentForm form = new DmTodoCommentForm();
        form.setTodoId("todo-1");
        form.setContent("received");

        assertThrows(BusinessException.class, () -> service.addComment(form));
    }

    @Test
    void shouldSaveCommentForCurrentStudent() {
        DmTodo todo = new DmTodo();
        todo.setId("todo-1");
        todo.setRoomId("room-101");
        todo.setDelflag(false);
        when(roomStudentService.getSelectedRoomId("stu-1")).thenReturn("room-101");
        when(dmTodoMapper.selectOne(any())).thenReturn(todo);

        DmTodoCommentServiceImpl spyService = spy(service);
        doReturn(true).when(spyService).save(any(DmTodoComment.class));

        DmTodoCommentForm form = new DmTodoCommentForm();
        form.setTodoId("todo-1");
        form.setContent("received");

        assertTrue(spyService.addComment(form));
        ArgumentCaptor<DmTodoComment> captor = ArgumentCaptor.forClass(DmTodoComment.class);
        verify(spyService).save(captor.capture());
        assertEquals("todo-1", captor.getValue().getTodoId());
        assertEquals("stu-1", captor.getValue().getCommenterStudentId());
        assertEquals("received", captor.getValue().getContent());
    }
}
