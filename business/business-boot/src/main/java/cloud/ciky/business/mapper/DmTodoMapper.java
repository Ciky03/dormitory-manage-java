package cloud.ciky.business.mapper;

import cloud.ciky.base.model.Option;
import cloud.ciky.business.model.entity.DmTodo;
import cloud.ciky.business.model.query.DmTodoPageQuery;
import cloud.ciky.business.model.vo.DmTodoDetailVO;
import cloud.ciky.business.model.vo.DmTodoPageVO;
import cloud.ciky.business.model.vo.DmTodoStatVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

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

    /**
     * <p>
     * 获取宿舍待办统计
     * </p>
     *
     * @author ciky
     * @since 2026/3/26 17:42
     * @param studentId 学生id
     * @param weekStart 周起始时间
     * @param weekEnd 周结束时间
     * @return cloud.ciky.business.model.vo.DmTodoStatVO
     */
    DmTodoStatVO selectTodoStat(@Param("studentId") String studentId,
                                @Param("weekStart") LocalDateTime weekStart,
                                @Param("weekEnd") LocalDateTime weekEnd);

    /**
     * <p>
     * 获取宿舍待办分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/26 17:42
     * @param page 分页对象
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.DmTodoPageVO>
     */
    Page<DmTodoPageVO> selectTodoPage(Page<DmTodoPageVO> page, @Param("query") DmTodoPageQuery query);

    /**
     * <p>
     * 获取宿舍待办详情
     * </p>
     *
     * @author ciky
     * @since 2026/3/26 17:42
     * @param id 主键
     * @param roomId 宿舍编号
     * @return cloud.ciky.business.model.vo.DmTodoDetailVO
     */
    DmTodoDetailVO selectTodoDetail(@Param("id") String id, @Param("roomId") String roomId);

    /**
     * <p>
     * 获取当前宿舍负责人候选项
     * </p>
     *
     * @author ciky
     * @since 2026/3/26 17:42
     * @param roomId 宿舍编号
     * @return java.util.List<cloud.ciky.base.model.Option<java.lang.String>>
     */
    List<Option<String>> listAssigneeOptions(@Param("roomId") String roomId);

    /**
     * <p>
     * 校验当前学生是否属于指定宿舍
     * </p>
     *
     * @author ciky
     * @since 2026/3/26 17:42
     * @param roomId 宿舍编号
     * @param studentId 学生编号
     * @return java.lang.Integer
     */
    Integer countCurrentRoomStudent(@Param("roomId") String roomId, @Param("studentId") String studentId);
}
