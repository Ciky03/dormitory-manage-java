package cloud.ciky.business.service;

import cloud.ciky.base.BaseQuery;
import cloud.ciky.business.model.entity.UserDm;
import cloud.ciky.business.model.form.UserDmForm;
import cloud.ciky.business.model.vo.DmPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 宿管表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-03-14 11:40:01
 */
public interface UserDmService extends IService<UserDm> {

    /**
     * <p>
     * 获取宿管分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.DmPageVO>
     */
    Page<DmPageVO> listDormitoryManager(BaseQuery query);

    /**
     * <p>
     * 获取宿管表单
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param id 主键
     * @return cloud.ciky.business.model.form.UserDmForm
     */
    UserDmForm getDormitoryManagerForm(String id);

    /**
     * <p>
     * 删除宿管
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param id 主键
     * @return boolean
     */
    boolean deleteDormitoryManager(String id);

    /**
     * <p>
     * 保存宿管
     * </p>
     *
     * @author ciky
     * @since 2026/3/15 11:33
     * @param form
     * @return boolean
     */
    boolean saveDormitoryManager(UserDmForm form);
}
