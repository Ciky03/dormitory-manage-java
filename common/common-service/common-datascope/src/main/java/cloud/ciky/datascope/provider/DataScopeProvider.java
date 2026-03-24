package cloud.ciky.datascope.provider;

import cloud.ciky.base.enums.DataScopeEnum;
import cloud.ciky.base.enums.UserTypeEnum;
import cloud.ciky.base.model.DataScope;
import cloud.ciky.datascope.service.DataScopeService;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

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
public class DataScopeProvider implements IDataScopeProvider {

    private final DataScopeService dataScopeService;

    public DataScope getCurrentUserDataScope() {
        String userId = SecurityUtils.getUserId();
        String businessUserId = SecurityUtils.getBusinessUserId();
        Integer userType = SecurityUtils.getUserType();
        DataScope ds = new DataScope();

        List<String> roles = dataScopeService.findRoleCodesByUserId(userId);

        if (roles.contains(UserTypeEnum.ADMIN.getCode())
                && Objects.equals(userType, UserTypeEnum.ADMIN.getValue())) {
            ds.setType(DataScopeEnum.ALL);
            return ds;
        }

        // 优先级：宿管 > 教师 > 学生
        // TODO redis缓存
        if (roles.contains(UserTypeEnum.DORMITORY_MANAGER.getCode())
                && Objects.equals(userType, UserTypeEnum.DORMITORY_MANAGER.getValue())) {
            List<String> buildingIds = dataScopeService.findBuildingIdsByDmId(businessUserId);
            if (CollUtil.isNotEmpty(buildingIds)) {
                ds.setType(DataScopeEnum.BUILDING);
                ds.setBuildingIds(buildingIds);
            }
            return ds;
        }

        if (roles.contains(UserTypeEnum.TEACHER.getCode())
                && Objects.equals(userType, UserTypeEnum.TEACHER.getValue())) {
            List<String> studentIds = dataScopeService.findStudentIdByTeacherId(businessUserId);
            if (CollUtil.isNotEmpty(studentIds)) {
                ds.setType(DataScopeEnum.STUDENT);
                ds.setStudentIds(studentIds);
            }
            return ds;
        }

        if (roles.contains(UserTypeEnum.STUDENT.getCode())
                && Objects.equals(userType, UserTypeEnum.STUDENT.getValue())) {
            String roomId = dataScopeService.findRoomIdByStudentId(businessUserId);
            if (CharSequenceUtil.isNotBlank(roomId)) {
                ds.setType(DataScopeEnum.ROOM);
                ds.setRoomIds(List.of(roomId));
            }
            return ds;
        }

        ds.setType(DataScopeEnum.NONE);
        return ds;
    }
}
