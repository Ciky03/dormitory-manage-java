package cloud.ciky.business.mapper;

import cloud.ciky.business.model.entity.RoomStudent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 学生宿舍关联表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-03-19 16:43:50
 */
@Mapper
public interface RoomStudentMapper extends BaseMapper<RoomStudent> {

}
