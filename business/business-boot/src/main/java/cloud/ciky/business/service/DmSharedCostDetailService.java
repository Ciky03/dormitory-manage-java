package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.DmSharedCostDetail;
import cloud.ciky.business.model.form.DmSharedCostMemberForm;
import cloud.ciky.business.model.vo.DmSharedCostMemberVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 宿舍费用公摊明细表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-03-31 12:04:45
 */
public interface DmSharedCostDetailService extends IService<DmSharedCostDetail> {

    /**
     * <p>
     * 重建公摊单明细
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 15:05
     * @param costId 公摊单id
     * @param memberList 成员分摊列表
     * @param userId 操作人
     * @return boolean
     */
    boolean replaceCostDetails(String costId, List<DmSharedCostMemberForm> memberList, String userId);

    /**
     * <p>
     * 逻辑删除公摊单全部明细
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 15:12
     * @param costId 公摊单id
     * @param userId 操作人
     * @return boolean
     */
    boolean deleteCostDetails(String costId, String userId);

    /**
     * <p>
     * 获取当前学生可支付的公摊明细
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 15:05
     * @param detailId 明细id
     * @param studentId 当前学生id
     * @return cloud.ciky.business.model.entity.DmSharedCostDetail
     */
    DmSharedCostDetail requirePayableDetail(String detailId, String studentId);

    /**
     * <p>
     * 支付公摊明细
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 15:05
     * @param detailId 明细id
     * @param studentId 当前学生id
     * @param voucherAttachId 缴费凭证附件id
     * @param userId 操作人
     * @return boolean
     */
    boolean payDetail(String detailId, String studentId, String voucherAttachId, String userId);

    /**
     * <p>
     * 统计公摊单未缴明细数量
     * </p>
     *
     * @author ciky
     * @since 2026/3/31 15:05
     * @param costId 公摊单id
     * @return java.lang.Integer
     */
    Integer countUnpaidDetail(String costId);

    /**
     * <p>
     * 查询费用公摊明细列表
     * </p>
     *
     * @author ciky
     * @since 2026/4/1 0:07
     * @param costId 公摊单id
     * @param studentId 学生id
     * @return java.util.List<cloud.ciky.business.model.vo.DmSharedCostMemberVO>
     */
    List<DmSharedCostMemberVO> listSharedCostMember(String costId, String studentId);
}
