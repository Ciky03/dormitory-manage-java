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
 * 宿舍待办服务类
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
public interface DmTodoService extends IService<DmTodo> {

    /**
     * <p>
     * 获取宿舍待办统计
     * </p>
     *
     * @author ciky
     * @since 2026/3/27 12:01
     * @return cloud.ciky.business.model.vo.DmTodoStatVO
     */
    DmTodoStatVO getTodoStat();

    /**
     * <p>
     * 获取宿舍待办分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/27 12:01
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.DmTodoPageVO>
     */
    Page<DmTodoPageVO> listTodo(DmTodoPageQuery query);

    /**
     * <p>
     * 获取宿舍待办详情
     * </p>
     *
     * @author ciky
     * @since 2026/3/27 12:01
     * @param id 待办事项id
     * @return cloud.ciky.business.model.vo.DmTodoDetailVO
     */
    DmTodoDetailVO getTodoDetail(String id);

    /**
     * <p>
     * 保存宿舍待办
     * </p>
     *
     * @author ciky
     * @since 2026/3/27 12:02
     * @param form 表单对象
     * @return boolean
     */
    boolean saveTodo(DmTodoForm form);

    /**
     * <p>
     * 删除宿舍待办
     * </p>
     *
     * @author ciky
     * @since 2026/3/27 12:02
     * @param id 待办事项id
     * @return boolean
     */
    boolean deleteTodo(String id);

    /**
     * <p>
     * 开始处理宿舍待办
     * </p>
     *
     * @author ciky
     * @since 2026/3/27 12:02
     * @param id 待办事项id
     * @return boolean
     */
    boolean startTodo(String id);

    /**
     * <p>
     * 完成宿舍待办
     * </p>
     *
     * @author ciky
     * @since 2026/3/27 12:03
     * @param id 待办事项id
     * @return boolean
     */
    boolean completeTodo(String id);

    /**
     * <p>
     * 取消宿舍待办
     * </p>
     *
     * @author ciky
     * @since 2026/3/27 12:03
     * @param id 待办事项id
     * @param cancelReason 取消原因
     * @return boolean
     */
    boolean cancelTodo(String id, String cancelReason);

    /**
     * <p>
     * 获取当前宿舍负责人候选项
     * </p>
     *
     * @author ciky
     * @since 2026/3/27 12:03
     * @return java.util.List<cloud.ciky.base.model.Option<java.lang.String>>
     */
    List<Option<String>> listAssigneeOptions();
}
