package cloud.ciky.business.mapper;

import cloud.ciky.business.model.entity.Dormitory;
import cloud.ciky.business.model.form.DormitoryForm;
import cloud.ciky.business.model.query.RoomPageQuery;
import cloud.ciky.business.model.query.RoomTreeQuery;
import cloud.ciky.business.model.vo.DormitoryTreeVO;
import cloud.ciky.business.model.vo.RoomPageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 宿舍表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-03-11 17:04:20
 */
@Mapper
public interface DormitoryMapper extends BaseMapper<Dormitory> {

    /**
     * <p>
     *  获取楼栋/宿舍列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/23 17:56
     * @param query 查询对象
     * @return java.util.List<cloud.ciky.business.model.vo.DormitoryTreeVO>
     */
    List<DormitoryTreeVO> selectBuildingRoomList(RoomTreeQuery query);

    /**
     * <p>
     * 获取宿舍分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/11 18:04
     * @param objectPage 分页对象
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.RoomPageVO>
     */
    Page<RoomPageVO> selectRoom(Page<Object> objectPage, RoomPageQuery query);

    /**
     * <p>
     * 获取宿舍表单
     * </p>
     *
     * @param id 主键
     * @return cloud.ciky.business.model.form.DormitoryForm
     * @author ciky
     * @since 2026/3/11 17:04
     */
    DormitoryForm selectRoomForm(String id);


}
