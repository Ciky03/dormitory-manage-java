package cloud.ciky.business.mapper;

import cloud.ciky.business.model.entity.DmConvention;
import cloud.ciky.business.model.vo.DmConventionVO;
import cloud.ciky.business.model.vo.HistoryConventionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 宿舍公约版本表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-03-24 19:32:06
 */
@Mapper
public interface DmConventionMapper extends BaseMapper<DmConvention> {

    /**
     * <p>
     * 查询指定宿舍公约
     * </p>
     *
     * @author ciky
     * @since 2026/3/25 11:23
     * @param studentId 学生id
     * @param conventionId 宿舍公约id (为null查询当前宿舍公约)
     * @return cloud.ciky.business.model.vo.DmConventionVO
     */
    DmConventionVO selectConventionInfo(String studentId, String conventionId);

    /**
     * <p>
     * 获取宿舍公约历史/草稿
     * </p>
     *
     * @author ciky
     * @since 2026/3/25 13:43
     * @param roomId 宿舍id
     * @return java.util.List<cloud.ciky.business.model.vo.HistoryConventionVO>
     */
    List<HistoryConventionVO> selectHistoryConvention(String roomId);
}
