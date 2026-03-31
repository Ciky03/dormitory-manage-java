package cloud.ciky.business.mapper;

import cloud.ciky.business.model.entity.DmSharedCost;
import cloud.ciky.business.model.query.DmSharedCostPageQuery;
import cloud.ciky.business.model.vo.DmSharedCostDetailVO;
import cloud.ciky.business.model.vo.DmSharedCostMemberVO;
import cloud.ciky.business.model.vo.DmSharedCostPageVO;
import cloud.ciky.business.model.vo.DmSharedCostStatVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DmSharedCostMapper extends BaseMapper<DmSharedCost> {

    /**
     * <p>
     * 获取宿舍费用公摊统计
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 23:53
     * @param studentId 学生id
     * @return cloud.ciky.business.model.vo.DmSharedCostStatVO
     */
    DmSharedCostStatVO selectSharedCostStat(@Param("studentId") String studentId);

    /**
     * <p>
     * 获取宿舍费用公摊分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 23:53
     * @param page 分页对象
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.DmSharedCostPageVO>
     */
    Page<DmSharedCostPageVO> selectSharedCostPage(Page<DmSharedCostPageVO> page, @Param("query") DmSharedCostPageQuery query);

    /**
     * <p>
     * 获取宿舍费用公摊详情
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 23:57
     * @param id 费用公摊id
     * @param roomId 宿舍id
     * @return cloud.ciky.business.model.vo.DmSharedCostDetailVO
     */
    DmSharedCostDetailVO selectSharedCostDetail(@Param("id") String id, @Param("roomId") String roomId);

    /**
     * <p>
     * 查询当前宿舍用户列表
     * </p>
     *
     * @author ciky
     * @since 2026/4/1 0:36
     * @param roomId 宿舍id
     * @return java.util.List<cloud.ciky.business.model.vo.DmSharedCostMemberVO>
     */
    List<DmSharedCostMemberVO> selectCurrentRoomMemberList(@Param("roomId") String roomId);

    /**
     * <p>
     * 判断学生是否属于当前宿舍
     * </p>
     *
     * @author ciky
     * @since 2026/4/1 0:37
     * @param roomId 宿舍id
     * @param studentId 学生id
     * @return java.lang.Integer
     */
    Integer countCurrentRoomStudent(@Param("roomId") String roomId,
                                    @Param("studentId") String studentId);

    /**
     * <p>
     * 查询当前费用公摊单未缴纳人员数量
     * </p>
     *
     * @author ciky
     * @since 2026/4/1 0:38
     * @param costId 费用公摊单id
     * @return java.lang.Integer
     */
    Integer countUnpaidDetail(@Param("costId") String costId);
}
