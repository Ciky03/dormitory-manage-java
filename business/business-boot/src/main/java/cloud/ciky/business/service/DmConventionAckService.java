package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.DmConventionAck;
import cloud.ciky.business.model.form.DmConventionAckForm;
import cloud.ciky.business.model.vo.DmConventionAckStateVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 宿舍公约阅读/同意记录表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-03-24 19:32:07
 */
public interface DmConventionAckService extends IService<DmConventionAck> {

    /**
     * <p>
     * 确认已读并同意宿舍公约
     * </p>
     *
     * @author ciky
     * @since 2026/3/25 00:34
     * @param form 表单对象
     * @return boolean
     */
    boolean ackConvention( DmConventionAckForm form);

    /**
     * <p>
     * 获取宿舍公约同意统计
     * </p>
     *
     * @author ciky
     * @since 2026/3/25 00:34
     * @param id 公约id
     * @return cloud.ciky.business.model.vo.DmConventionAckStatVO
     */
    DmConventionAckStateVO getAckStat(String id);

}
