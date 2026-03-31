package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.DmSharedCost;
import cloud.ciky.business.model.form.DmSharedCostForm;
import cloud.ciky.business.model.form.DmSharedCostPayForm;
import cloud.ciky.business.model.query.DmSharedCostPageQuery;
import cloud.ciky.business.model.vo.DmSharedCostDetailVO;
import cloud.ciky.business.model.vo.DmSharedCostPageVO;
import cloud.ciky.business.model.vo.DmSharedCostStatVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 宿舍费用公摊主表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-03-31 12:04:45
 */
public interface DmSharedCostService extends IService<DmSharedCost> {

    /**
     * <p>
     * 获取宿舍费用公摊统计
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 15:08
     * @return cloud.ciky.business.model.vo.DmSharedCostStatVO
     */
    DmSharedCostStatVO getSharedCostStat();

    /**
     * <p>
     * 获取宿舍费用公摊分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 15:08
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.DmSharedCostPageVO>
     */
    Page<DmSharedCostPageVO> listSharedCost(DmSharedCostPageQuery query);

    /**
     * <p>
     * 获取宿舍费用公摊详情
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 15:08
     * @param id 公摊单id
     * @return cloud.ciky.business.model.vo.DmSharedCostDetailVO
     */
    DmSharedCostDetailVO getSharedCostDetail(String id);

    /**
     * <p>
     * 保存宿舍费用公摊
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 15:08
     * @param form 表单对象
     * @return boolean
     */
    boolean saveSharedCost(DmSharedCostForm form);

    /**
     * <p>
     * 发布宿舍费用公摊
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 15:08
     * @param id 公摊单id
     * @return boolean
     */
    boolean publishSharedCost(String id);

    /**
     * <p>
     * 支付宿舍费用公摊
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 15:08
     * @param detailId 明细id
     * @param form 表单对象
     * @return boolean
     */
    boolean paySharedCost(String detailId, DmSharedCostPayForm form);

    /**
     * <p>
     * 取消宿舍费用公摊
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 15:08
     * @param id 公摊单id
     * @return boolean
     */
    boolean cancelSharedCost(String id);

    /**
     * <p>
     * 删除宿舍费用公摊
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 15:08
     * @param id 公摊单id
     * @return boolean
     */
    boolean deleteSharedCost(String id);
}
