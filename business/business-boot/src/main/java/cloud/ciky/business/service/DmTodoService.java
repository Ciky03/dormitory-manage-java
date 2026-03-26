package cloud.ciky.business.service;

import cloud.ciky.base.model.Option;
import cloud.ciky.business.model.entity.DmTodo;
import cloud.ciky.business.model.form.DmTodoForm;
import cloud.ciky.business.model.query.DmTodoPageQuery;
import cloud.ciky.business.model.vo.DmTodoDetailVO;
import cloud.ciky.business.model.vo.DmTodoPageVO;
import cloud.ciky.business.model.vo.DmTodoStatVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Todo service.
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
public interface DmTodoService extends IService<DmTodo> {

    DmTodoStatVO getTodoStat();

    Page<DmTodoPageVO> listTodo(DmTodoPageQuery query);

    DmTodoDetailVO getTodoDetail(String id);

    boolean saveTodo(DmTodoForm form);

    boolean deleteTodo(String id);

    boolean startTodo(String id);

    boolean completeTodo(String id);

    boolean cancelTodo(String id, String cancelReason);

    List<Option<String>> listAssigneeOptions();
}
