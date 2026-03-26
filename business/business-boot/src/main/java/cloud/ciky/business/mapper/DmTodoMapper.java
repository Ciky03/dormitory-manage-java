package cloud.ciky.business.mapper;

import cloud.ciky.business.model.entity.DmTodo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 宿舍待办主表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
@Mapper
public interface DmTodoMapper extends BaseMapper<DmTodo> {

}
