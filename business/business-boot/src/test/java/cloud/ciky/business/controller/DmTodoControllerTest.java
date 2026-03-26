package cloud.ciky.business.controller;

import cloud.ciky.base.model.Option;
import cloud.ciky.business.model.query.DmTodoPageQuery;
import cloud.ciky.business.service.DmTodoCommentService;
import cloud.ciky.business.service.DmTodoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DmTodoControllerTest {

    @Mock
    private DmTodoService dmTodoService;

    @Mock
    private DmTodoCommentService dmTodoCommentService;

    @InjectMocks
    private DmTodoController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldMapStatusQueryParamIntoQueryState() throws Exception {
        when(dmTodoService.listTodo(any())).thenReturn(new Page<>(1, 10, 0));

        mockMvc.perform(get("/dorm/todo/list")
                        .param("keywords", "\u536b\u751f")
                        .param("status", "2")
                        .param("priority", "3")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());

        ArgumentCaptor<DmTodoPageQuery> captor = ArgumentCaptor.forClass(DmTodoPageQuery.class);
        verify(dmTodoService).listTodo(captor.capture());
        assertEquals("\u536b\u751f", captor.getValue().getKeywords());
        assertEquals(2, captor.getValue().getState());
        assertEquals(3, captor.getValue().getPriority());
    }

    @Test
    void shouldExposeAssigneeOptionsEndpoint() throws Exception {
        when(dmTodoService.listAssigneeOptions()).thenReturn(List.of(new Option<>("stu-1", "\u5f20\u4e09")));

        mockMvc.perform(get("/dorm/todo/assignee/options"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].value").value("stu-1"))
                .andExpect(jsonPath("$.data[0].label").value("\u5f20\u4e09"));
    }
}
