package cloud.ciky.business.mapper;

import cloud.ciky.business.model.entity.EduUnit;
import cloud.ciky.business.model.form.EduUnitForm;
import cloud.ciky.business.model.query.ClassPageQuery;
import cloud.ciky.business.model.vo.ClassPageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 学院/专业/班级表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-02-05 17:03:26
 */
@Mapper
public interface EduUnitMapper extends BaseMapper<EduUnit> {
    /**
     * <p>
     * 获取班级列表
     * </p>
     *
     * @author ciky
     * @since 2026/2/5 18:15
     * @param page 分页对象
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.ClassPageVO>
     */
    Page<ClassPageVO> listClass(Page<Object> page, ClassPageQuery query);

    /**
     * <p>
     * 获取学院/专业/班级表单
     * </p>
     *
     * @author ciky
     * @since 2026/3/11 13:44
     * @param id 主键
     * @return cloud.ciky.business.model.form.EduUnitForm
     */
    EduUnitForm selectUnitForm(String id);
}
