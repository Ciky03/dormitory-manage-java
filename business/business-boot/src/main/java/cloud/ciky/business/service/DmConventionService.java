package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.DmConvention;
import cloud.ciky.business.model.form.DmConventionForm;
import cloud.ciky.business.model.vo.DmConventionVO;
import cloud.ciky.business.model.vo.HistoryConventionVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 宿舍公约版本表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-03-24 19:32:06
 */
public interface DmConventionService extends IService<DmConvention> {

    /**
     * <p>
     * 获取当前生效宿舍公约
     * </p>
     *
     * @author ciky
     * @since 2026/3/25 00:33
     * @return cloud.ciky.business.model.vo.DmConventionVO
     */
    DmConventionVO getCurrentConvention();

    /**
     * <p>
     * 获取宿舍公约历史/草稿
     * </p>
     *
     * @author ciky
     * @since 2026/3/25 13:41
     * @param roomId 宿舍id
     * @return java.util.List<cloud.ciky.business.model.vo.HistoryConventionVO>
     */
    List<HistoryConventionVO> getHistoryConvention(String roomId);

    /**
     * <p>
     * 回显宿舍公约
     * </p>
     *
     * @author ciky
     * @since 2026/3/25 14:31
     * @param id 宿舍公约id
     * @return cloud.ciky.business.model.vo.DmConventionVO
     */
    DmConventionVO getConventionForm(String id);

    /**
     * <p>
     * 保存宿舍公约
     * </p>
     *
     * @author ciky
     * @since 2026/3/25 14:15
     * @param form 表单对象
     * @return boolean
     */
    boolean saveConvention(DmConventionForm form);

    /**
     * <p>
     * 发布宿舍公约草稿
     * </p>
     *
     * @author ciky
     * @since 2026/3/25 00:33
     * @param id 主键
     * @return boolean
     */
    boolean publishConvention(String id);

    /**
     * <p>
     * 获取当前登录用户宿舍下的公约
     * </p>
     *
     * @author ciky
     * @since 2026/3/25 00:33
     * @param id 公约id
     * @return cloud.ciky.business.model.entity.DmConvention
     */
    DmConvention getCurrentRoomConvention(String id);

}
