package cloud.ciky.business.mapper;

import cloud.ciky.business.model.entity.ClassTeacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 班级教师关联表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-03-17 11:25:44
 */
@Mapper
public interface ClassTeacherMapper extends BaseMapper<ClassTeacher> {

}
