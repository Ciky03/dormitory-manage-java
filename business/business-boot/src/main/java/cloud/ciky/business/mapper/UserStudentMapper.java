package cloud.ciky.business.mapper;

import cloud.ciky.business.model.entity.UserStudent;
import cloud.ciky.business.model.form.UserStudentForm;
import cloud.ciky.business.model.query.StudentPageQuery;
import cloud.ciky.business.model.vo.StudentPageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 学生表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-03-12 17:53:23
 */
@Mapper
public interface UserStudentMapper extends BaseMapper<UserStudent> {

    /**
     * <p>
     * 获取学生分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param objectPage 分页对象
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.StudentPageVO>
     */
    Page<StudentPageVO> selectStudentPage(Page<Object> objectPage, StudentPageQuery query);

    /**
     * <p>
     * 获取学生表单
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param id 主键
     * @return cloud.ciky.business.model.form.UserStudentForm
     */
    UserStudentForm selectStudentForm(String id);
}
