package cloud.ciky.business.service.impl;

import cloud.ciky.base.enums.DelflagEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.business.mapper.UserStudentMapper;
import cloud.ciky.business.model.entity.UserDormitoryManager;
import cloud.ciky.business.model.entity.UserStudent;
import cloud.ciky.business.model.form.UserStudentForm;
import cloud.ciky.business.model.query.StudentPageQuery;
import cloud.ciky.business.model.vo.StudentPageVO;
import cloud.ciky.business.service.UserStudentService;
import cloud.ciky.security.util.SecurityUtils;
import cloud.ciky.system.api.UserFeignClient;
import cloud.ciky.system.enums.UserTypeEnum;
import cloud.ciky.system.model.form.UserForm;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 学生表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-12 17:53:23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserStudentServiceImpl extends ServiceImpl<UserStudentMapper, UserStudent> implements UserStudentService {

    private final UserFeignClient userFeignClient;

    @Override
    public Page<StudentPageVO> listStudent(StudentPageQuery query) {
        return this.baseMapper.selectStudentPage(new Page<>(query.getPageNum(), query.getPageSize()), query);
    }

    @Override
    public UserStudentForm getStudentForm(String id) {
        return this.baseMapper.selectStudentForm(id);
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public boolean deleteStudent(String id) {
        String optUser = SecurityUtils.getUserId();
        // 逻辑删除,方便溯源
        boolean update = this.update(new LambdaUpdateWrapper<UserStudent>()
                .set(UserStudent::getDelflag, DelflagEnum.REMOVED.getValue())
                .set(UserStudent::getUpdateBy, optUser)
                .set(UserStudent::getUpdateTime, LocalDateTime.now())
                .eq(UserStudent::getId, id));

        // 系统用户表解绑
        if (update) {
            update = userFeignClient.unbindBusiness(id).getData();
        }
        return update;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveStudent(UserStudentForm form) {
        String optUser = SecurityUtils.getUserId();
        String studentId = form.getId();
        String studentNum = form.getStudentNum();
        String realName = form.getRealName();

        long studentCount = this.count(new LambdaQueryWrapper<UserStudent>()
                .ne(UserStudent::getId, studentId)
                .eq(UserStudent::getStudentNum, studentNum)
                .eq(UserStudent::getDelflag, DelflagEnum.USABLE.getValue()));
        if (studentCount > 0) {
            throw new BusinessException("该学号已存在，请修改后重试!");
        }

        UserStudent entity = new UserStudent();
        if (CharSequenceUtil.isBlank(studentId)) {
            entity.setCreateBy(optUser);
        } else {
            entity.setId(studentId);
            entity.setUpdateBy(optUser);
        }
        entity.setRealName(realName);
        entity.setStudentNum(studentNum);
        entity.setAdmissionYear(form.getAdmissionYear());
        entity.setGraduationYear(form.getGraduationYear());
        boolean saved = this.saveOrUpdate(entity);

        if (saved) {
            // 构造用户表单, 保存用户
            UserForm userForm = form.getUserForm();
            userForm.setUserType(UserTypeEnum.STUDENT.getValue());
            userForm.setBusinessUserId(entity.getId());
            userForm.setRealName(realName);

            saved = userFeignClient.addUser(userForm).getData();
        }
        return saved;
    }
}
