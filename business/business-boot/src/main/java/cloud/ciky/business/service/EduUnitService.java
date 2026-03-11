package cloud.ciky.business.service;

import cloud.ciky.base.result.PageResult;
import cloud.ciky.business.model.entity.EduUnit;
import cloud.ciky.business.model.form.EduUnitForm;
import cloud.ciky.business.model.query.ClassPageQuery;
import cloud.ciky.business.model.vo.ClassPageVO;
import cloud.ciky.business.model.vo.EduUnitVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 学院/专业/班级表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-02-05 17:03:26
 */
public interface EduUnitService extends IService<EduUnit> {

    /**
     * <p>
     * 获取学院/专业列表树
     * </p>
     *
     * @author ciky
     * @since 2026/2/5 17:27
     * @return java.util.List<cloud.ciky.business.model.vo.EduUnitVO>
     */
    List<EduUnitVO> listCollegeMajorTree();

    /**
     * <p>
     * 获取班级列表
     * </p>
     *
     * @author ciky
     * @since 2026/2/5 17:48
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.ClassPageVO>
     */
    Page<ClassPageVO> listClass(ClassPageQuery query);

    /**
     * <p>
     * 保存学院/专业/班级
     * </p>
     *
     * @author ciky
     * @since 2026/3/10 18:09
     * @param form 表单对象
     * @return boolean
     */
    boolean saveUnit(EduUnitForm form);

    /**
     * <p>
     * 获取学院/专业/班级表单
     * </p>
     *
     * @author ciky
     * @since 2026/3/11 11:06
     * @param id 主键
     * @return cloud.ciky.business.model.form.EduUnitForm
     */
    EduUnitForm getUnitForm(String id);

    /**
     * <p>
     * 删除学院/专业/班级
     * </p>
     *
     * @author ciky
     * @since 2026/3/11 16:03
     * @param id 主键
     * @return boolean
     */
    boolean deleteUnit(String id);
}
