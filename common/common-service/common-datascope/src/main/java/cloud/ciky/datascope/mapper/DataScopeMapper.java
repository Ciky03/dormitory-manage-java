package cloud.ciky.datascope.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 数据权限 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2025-12-15 16:10:58
 */
@Mapper
public interface DataScopeMapper {

    /**
     * <p>
     * 查询用户角色code
     * </p>
     *
     * @author ciky
     * @since 2025/12/17 12:03
     * @param userId
     * @return java.util.List<java.lang.String>
     */
    List<String> findRoleCodesByUserId(String userId);

    /**
     * <p>
     * 根据宿管id查询楼栋id
     * </p>
     *
     * @author ciky
     * @since 2026/3/24 16:43
     * @param dmId 宿管id
     * @return java.util.List<java.lang.String>
     */
    List<String> findBuildingIdsByDmId(String dmId);

    /**
     * <p>
     * 根据教师id查询学生id
     * </p>
     *
     * @author ciky
     * @since 2026/3/24 16:43
     * @param teacherId 教师id
     * @return java.util.List<java.lang.String>
     */
    List<String> findStudentIdByTeacherId(String teacherId);

    /**
     * <p>
     * 根据学生id查询宿舍id
     * </p>
     *
     * @author ciky
     * @since 2026/3/24 16:43
     * @param studentId 学生id
     * @return java.lang.String
     */
    String findRoomIdByStudentId(String studentId);
}
