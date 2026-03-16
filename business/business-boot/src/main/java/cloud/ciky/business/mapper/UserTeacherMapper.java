package cloud.ciky.business.mapper;

import cloud.ciky.base.BaseQuery;
import cloud.ciky.business.model.entity.UserTeacher;
import cloud.ciky.business.model.form.UserTeacherForm;
import cloud.ciky.business.model.vo.TeacherPageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 教师表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-03-14 11:40:01
 */
@Mapper
public interface UserTeacherMapper extends BaseMapper<UserTeacher> {

    /**
     * <p>
     * 获取教师分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param objectPage 分页对象
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.TeacherPageVO>
     */
    Page<TeacherPageVO> selectTeacherPage(Page<Object> objectPage, BaseQuery query);

    /**
     * <p>
     * 获取教师表单
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param id 主键
     * @return cloud.ciky.business.model.form.UserTeacherForm
     */
    UserTeacherForm selectTeacherForm(String id);
}
