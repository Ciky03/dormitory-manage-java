package cloud.ciky.business.utils;

import cloud.ciky.base.enums.UserTypeEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.text.CharSequenceUtil;

import java.util.Objects;

/**
 * <p>
 * 用户信息获取工具类
 * </p>
 *
 * @author ciky
 * @since 2026-03-26 11:35
 */
public class UserInfoUtil {

    public static String getCurrentStudentId() {
        if (!Objects.equals(SecurityUtils.getUserType(), UserTypeEnum.STUDENT.getValue())) {
            throw new BusinessException("当前用户不是学生");
        }
        String studentId = SecurityUtils.getBusinessUserId();
        if (CharSequenceUtil.isBlank(studentId)) {
            throw new BusinessException("未识别到当前学生信息");
        }
        return studentId;
    }

    public static String getCurrentTeacherId() {
        if (!Objects.equals(SecurityUtils.getUserType(), UserTypeEnum.TEACHER.getValue())) {
            throw new BusinessException("当前用户不是教师");
        }
        String teacherId = SecurityUtils.getBusinessUserId();
        if (CharSequenceUtil.isBlank(teacherId)) {
            throw new BusinessException("未识别到当前教师信息");
        }
        return teacherId;
    }

    public static String getCurrentDmId() {
        if (!Objects.equals(SecurityUtils.getUserType(), UserTypeEnum.DORMITORY_MANAGER.getValue())) {
            throw new BusinessException("当前用户不是宿管");
        }
        String dmId = SecurityUtils.getBusinessUserId();
        if (CharSequenceUtil.isBlank(dmId)) {
            throw new BusinessException("未识别到当前宿管信息");
        }
        return dmId;
    }

}
