package cloud.ciky.business.mapper;

import cloud.ciky.business.model.entity.DmSharedCostDetail;
import cloud.ciky.business.model.vo.DmSharedCostMemberVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 宿舍费用公摊明细表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-03-31 12:04:45
 */
@Mapper
public interface DmSharedCostDetailMapper extends BaseMapper<DmSharedCostDetail> {

    /**
     * <p>
     * 查询费用公摊明细列表
     * </p>
     *
     * @author ciky
     * @since 2026/4/1 0:09
     * @param costId 公摊单id
     * @param studentId 学生id
     * @return java.util.List<cloud.ciky.business.model.vo.DmSharedCostMemberVO>
     */
    List<DmSharedCostMemberVO> listSharedCostMember(String costId, String studentId);
}
