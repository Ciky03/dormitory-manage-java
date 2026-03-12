package cloud.ciky.business.service;

import cloud.ciky.base.BasePageQuery;
import cloud.ciky.base.model.KeyValue;
import cloud.ciky.business.model.entity.DmRoom;
import cloud.ciky.business.model.form.DmRoomForm;
import cloud.ciky.business.model.query.RoomPageQuery;
import cloud.ciky.business.model.vo.RoomPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 宿舍表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-03-11 17:04:20
 */
public interface DmRoomService extends IService<DmRoom> {

    /**
     * <p>
     * 获取楼栋列表 (key:id, value:楼栋名)
     * </p>
     *
     * @author ciky
     * @since 2026/3/11 17:43
     * @return java.util.List<cloud.ciky.base.model.KeyValue>
     */
    List<KeyValue> listBuilding();

    /**
     * <p>
     * 获取宿舍分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/11 18:03
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.RoomPageVO>
     */
    Page<RoomPageVO> listRoom(RoomPageQuery query);

    /**
     * <p>
     * 获取楼栋/宿舍表单
     * </p>
     *
     * @param id 主键
     * @return cloud.ciky.business.model.form.DmRoomForm
     * @author ciky
     * @since 2026/3/11 17:04
     */
    DmRoomForm getRoomForm(String id);

    /**
     * <p>
     * 保存楼栋/宿舍
     * </p>
     *
     * @param form 表单对象
     * @return boolean
     * @author ciky
     * @since 2026/3/11 17:04
     */
    boolean saveRoom(DmRoomForm form);

    /**
     * <p>
     * 删除楼栋/宿舍
     * </p>
     *
     * @param id 主键
     * @return boolean
     * @author ciky
     * @since 2026/3/11 17:04
     */
    boolean deleteRoom(String id);

}
