package cloud.ciky.datascope.provider;

import cloud.ciky.base.enums.DataScopeEnum;
import cloud.ciky.base.enums.RoleEnum;
import cloud.ciky.base.model.DataScope;
import cloud.ciky.datascope.service.DataScopeService;
import cloud.ciky.datascope.service.impl.DataScopeServiceImpl;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 数据权限提供器 实现类
 * </p>
 *
 * @author ciky
 * @since 2025-12-16 16:34
 */
@Component
@RequiredArgsConstructor
public class DataScopeProvider implements IDataScopeProvider{

    private final DataScopeService dataScopeService;

    public DataScope getCurrentUserDataScope() {
        String userId = SecurityUtils.getUserId();
        DataScope ds = new DataScope();

        List<String> roles = dataScopeService.findRoleCodesByUserId(userId);

        if (roles.contains(RoleEnum.ADMIN.getValue())) {
            ds.setType(DataScopeEnum.ALL);
            return ds;
        }

        // 优先级：宿管 > 教师 > 学生
        // TODO redis缓存
        if (roles.contains(RoleEnum.DORM_MANAGER.getValue())) {
//            List<Long> buildingIds = dormManagerBuildingMapper.findBuildingIdsByManager(userId);
            List<String> buildingIds = Arrays.asList("ciky");
            if (CollUtil.isEmpty(buildingIds)) {
                ds.setType(DataScopeEnum.NONE);
            } else {
                ds.setType(DataScopeEnum.BUILDING);
                ds.setBuildingIds(buildingIds);
            }
            return ds;
        }

        if (roles.contains(RoleEnum.TEACHER.getValue())) {
//            List<Long> studentIds = teacherStudentMapper.findStudentUserIdsByTeacher(userId);
            List<String> studentIds = Arrays.asList("987654");
            if (CollUtil.isEmpty(studentIds)) {
                ds.setType(DataScopeEnum.NONE);
            } else {
                ds.setType(DataScopeEnum.STUDENT);
                ds.setStudentIds(studentIds);
            }
            return ds;
        }

        if (roles.contains(RoleEnum.STUDENT.getValue())) {
//            Long roomId = studentAccommodationMapper.findCurrentRoomIdByStudent(userId);
            String roomId = "567890";
            if (CharSequenceUtil.isEmpty(roomId)) {
                ds.setType(DataScopeEnum.NONE);
            } else {
                ds.setType(DataScopeEnum.ROOM);
                ds.setRoomIds(List.of(roomId));
            }
            return ds;
        }

        ds.setType(DataScopeEnum.NONE);
        return ds;
    }
}
