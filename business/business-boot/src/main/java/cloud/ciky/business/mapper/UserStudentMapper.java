package cloud.ciky.business.mapper;

import cloud.ciky.business.model.entity.UserStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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

}
