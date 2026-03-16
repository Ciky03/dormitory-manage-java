package cloud.ciky.business.service.impl;

import cloud.ciky.base.BaseQuery;
import cloud.ciky.base.enums.DelflagEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.business.mapper.UserTeacherMapper;
import cloud.ciky.business.model.entity.UserTeacher;
import cloud.ciky.business.model.form.UserTeacherForm;
import cloud.ciky.business.model.vo.TeacherPageVO;
import cloud.ciky.business.service.UserTeacherService;
import cloud.ciky.security.util.SecurityUtils;
import cloud.ciky.system.api.UserFeignClient;
import cloud.ciky.system.enums.UserTypeEnum;
import cloud.ciky.system.model.form.UserForm;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 教师表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-14 11:40:01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserTeacherServiceImpl extends ServiceImpl<UserTeacherMapper, UserTeacher> implements UserTeacherService {

    private final UserFeignClient userFeignClient;

    @Override
    public Page<TeacherPageVO> listTeacher(BaseQuery query) {
        return this.baseMapper.selectTeacherPage(new Page<>(query.getPageNum(), query.getPageSize()), query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTeacher(UserTeacherForm form) {
        String optUser = SecurityUtils.getUserId();
        String teacherId = form.getId();
        String teacherNum = form.getTeacherNum();
        String realName = form.getRealName();

        long teacherCount = this.count(new LambdaQueryWrapper<UserTeacher>()
                .ne(UserTeacher::getId, teacherId)
                .eq(UserTeacher::getTeacherNum, teacherNum)
                .eq(UserTeacher::getDelflag, DelflagEnum.USABLE.getValue()));
        if (teacherCount > 0) {
            throw new BusinessException("该工号已存在，请修改后重试!");
        }

        UserTeacher entity = new UserTeacher();
        if (CharSequenceUtil.isBlank(teacherId)) {
            entity.setCreateBy(optUser);
        } else {
            entity.setId(teacherId);
            entity.setUpdateBy(optUser);
        }
        entity.setRealName(realName);
        entity.setTeacherNum(teacherNum);
        entity.setEntryDate(form.getEntryDate());
        boolean saved = this.saveOrUpdate(entity);

        if (saved) {
            // 构造用户表单, 保存用户
            UserForm userForm = form.getUserForm();
            userForm.setUserType(UserTypeEnum.TEACHER.getValue());
            userForm.setBusinessUserId(entity.getId());
            userForm.setRealName(realName);

            saved = userFeignClient.addUser(userForm).getData();
        }
        return saved;
    }
}
