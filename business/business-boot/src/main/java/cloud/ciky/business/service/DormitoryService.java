package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.Dormitory;
import cloud.ciky.business.model.form.DormitoryForm;
import cloud.ciky.business.model.query.RoomPageQuery;
import cloud.ciky.business.model.query.RoomTreeQuery;
import cloud.ciky.business.model.vo.DormitoryTreeVO;
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
public interface DormitoryService extends IService<Dormitory> {

    /**
     * <p>
     * 获取楼栋/宿舍树
     * </p>
     *
     * @author ciky
     * @since 2026/3/19 17:04
     * @return java.util.List<cloud.ciky.business.model.vo.DormitoryTreeVO>
     */
    List<DormitoryTreeVO> listBuildingRoomTree(RoomTreeQuery query);

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
     * @return cloud.ciky.business.model.form.DormitoryForm
     * @author ciky
     * @since 2026/3/11 17:04
     */
    DormitoryForm getRoomForm(String id);

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
    boolean saveRoom(DormitoryForm form);

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
