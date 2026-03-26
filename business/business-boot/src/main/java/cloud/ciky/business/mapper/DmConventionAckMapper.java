package cloud.ciky.business.mapper;

import cloud.ciky.business.model.entity.DmConventionAck;
import cloud.ciky.business.model.vo.DmMemberConventionAckVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 宿舍公约阅读/同意记录表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-03-24 19:32:07
 */
@Mapper
public interface DmConventionAckMapper extends BaseMapper<DmConventionAck> {

    /**
     * <p>
     * 获取宿舍成员合约同意情况列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/26 0:37
     * @param conventionId 合约id
     * @return java.util.List<cloud.ciky.business.model.vo.DmMemberConventionAckVO>
     */
    List<DmMemberConventionAckVO> listMemberConventionAck(String conventionId);
}
