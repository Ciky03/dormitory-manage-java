package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.DmTodoComment;
import cloud.ciky.business.model.form.DmTodoCommentForm;
import cloud.ciky.business.model.vo.DmTodoCommentVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 宿舍待办评论服务类
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
public interface DmTodoCommentService extends IService<DmTodoComment> {

    /**
     * <p>
     * 获取宿舍待办评论列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/30 23:20
     * @param todoId
     * @return java.util.List<cloud.ciky.business.model.vo.DmTodoCommentVO>
     */
    List<DmTodoCommentVO> listComment(String todoId);

    /**
     * <p>
     * 新增宿舍待办评论
     * </p>
     *
     * @author ciky
     * @since 2026/3/30 23:20
     * @param form
     * @return boolean
     */
    boolean addComment(DmTodoCommentForm form);
}
