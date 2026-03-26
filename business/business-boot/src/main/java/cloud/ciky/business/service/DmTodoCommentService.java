package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.DmTodoComment;
import cloud.ciky.business.model.form.DmTodoCommentForm;
import cloud.ciky.business.model.vo.DmTodoCommentVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Todo comment service.
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
public interface DmTodoCommentService extends IService<DmTodoComment> {

    List<DmTodoCommentVO> listComment(String todoId);

    boolean addComment(DmTodoCommentForm form);
}
