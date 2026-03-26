package cloud.ciky.business.mapper;

import cloud.ciky.business.model.entity.DmTodoComment;
import cloud.ciky.business.model.vo.DmTodoCommentVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Todo comment mapper.
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
@Mapper
public interface DmTodoCommentMapper extends BaseMapper<DmTodoComment> {

    List<DmTodoCommentVO> listCommentByTodoId(@Param("todoId") String todoId);
}
