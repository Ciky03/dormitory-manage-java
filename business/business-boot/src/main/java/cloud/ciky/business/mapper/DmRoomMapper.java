package cloud.ciky.business.mapper;

import cloud.ciky.business.model.entity.DmRoom;
import cloud.ciky.business.model.form.DmRoomForm;
import cloud.ciky.business.model.query.RoomPageQuery;
import cloud.ciky.business.model.vo.RoomPageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 宿舍表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-03-11 17:04:20
 */
@Mapper
public interface DmRoomMapper extends BaseMapper<DmRoom> {

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
     * @return cloud.ciky.business.model.form.DmRoomForm
     * @author ciky
     * @since 2026/3/11 17:04
     */
    DmRoomForm selectRoomForm(String id);

    
}
