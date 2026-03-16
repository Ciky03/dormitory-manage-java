package cloud.ciky.business.service.impl;

import cloud.ciky.base.BaseQuery;
import cloud.ciky.base.enums.DelflagEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.business.mapper.UserDormitoryManagerMapper;
import cloud.ciky.business.model.entity.UserDormitoryManager;
import cloud.ciky.business.model.form.UserDormitoryManagerForm;
import cloud.ciky.business.model.vo.DormitoryManagerPageVO;
import cloud.ciky.business.service.UserDormitoryManagerService;
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
 * 宿管表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-14 11:40:01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserDormitoryManagerServiceImpl extends ServiceImpl<UserDormitoryManagerMapper, UserDormitoryManager> implements UserDormitoryManagerService {

    private final UserFeignClient userFeignClient;

    @Override
    public Page<DormitoryManagerPageVO> listDormitoryManager(BaseQuery query) {
        return this.baseMapper.selectDormitoryManagerPage(new Page<>(query.getPageNum(), query.getPageSize()), query);
    }

    @Override
    @Transactional
    public boolean saveDormitoryManager(UserDormitoryManagerForm form) {
        String optUser = SecurityUtils.getUserId();
        String dmId = form.getId();
        String dmNum = form.getDmNum();
        String realName = form.getRealName();

        long dmCount = this.count(new LambdaQueryWrapper<UserDormitoryManager>()
                .ne(UserDormitoryManager::getId, dmId)
                .eq(UserDormitoryManager::getDmNum, dmNum)
                .eq(UserDormitoryManager::getDelflag, DelflagEnum.USABLE.getValue()));
        if (dmCount > 0) {
            throw new BusinessException("该工号已存在，请修改后重试!");
        }

        UserDormitoryManager entity = new UserDormitoryManager();
        if (CharSequenceUtil.isBlank(dmId)) {
            entity.setCreateBy(optUser);
        } else {
            entity.setId(dmId);
            entity.setUpdateBy(optUser);
        }
        entity.setRealName(realName);
        entity.setDmNum(dmNum);
        entity.setEntryDate(form.getEntryDate());
        boolean saved = this.saveOrUpdate(entity);

        if (saved) {
            // 构造用户表单, 保存用户
            UserForm userForm = form.getUserForm();
            userForm.setUserType(UserTypeEnum.DORMITORY_MANAGER.getValue());
            userForm.setBusinessUserId(entity.getId());
            userForm.setRealName(realName);

            saved = userFeignClient.addUser(userForm).getData();
        }
        return saved;
    }
}
