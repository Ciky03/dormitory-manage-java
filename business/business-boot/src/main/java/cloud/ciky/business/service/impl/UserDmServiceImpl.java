package cloud.ciky.business.service.impl;

import cloud.ciky.base.BaseQuery;
import cloud.ciky.base.enums.DelflagEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.business.mapper.UserDmMapper;
import cloud.ciky.business.model.entity.UserDm;
import cloud.ciky.business.model.form.UserDmForm;
import cloud.ciky.business.model.vo.DmPageVO;
import cloud.ciky.business.service.UserDmService;
import cloud.ciky.security.util.SecurityUtils;
import cloud.ciky.system.api.UserFeignClient;
import cloud.ciky.base.enums.UserTypeEnum;
import cloud.ciky.system.model.form.UserForm;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
public class UserDmServiceImpl extends ServiceImpl<UserDmMapper, UserDm> implements UserDmService {

    private final UserFeignClient userFeignClient;

    @Override
    public Page<DmPageVO> listDormitoryManager(BaseQuery query) {
        return this.baseMapper.selectDormitoryManagerPage(new Page<>(query.getPageNum(), query.getPageSize()), query);
    }

    @Override
    public UserDmForm getDormitoryManagerForm(String id) {
        return this.baseMapper.selectDormitoryManagerForm(id);
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public boolean deleteDormitoryManager(String id) {
        String optUser = SecurityUtils.getUserId();
        // 逻辑删除,方便溯源
        boolean update = this.update(new LambdaUpdateWrapper<UserDm>()
                .set(UserDm::getDelflag, DelflagEnum.REMOVED.getValue())
                .set(UserDm::getUpdateBy, optUser)
                .set(UserDm::getUpdateTime, LocalDateTime.now())
                .eq(UserDm::getId, id));

        // 系统用户表解绑
        if (update) {
            update = userFeignClient.unbindBusiness(id).getData();
        }
        return update;
    }

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public boolean saveDormitoryManager(UserDmForm form) {
        String optUser = SecurityUtils.getUserId();
        String dmId = form.getId();
        String dmNum = form.getDmNum();
        String realName = form.getRealName();

        long dmCount = this.count(new LambdaQueryWrapper<UserDm>()
                .ne(UserDm::getId, dmId)
                .eq(UserDm::getDmNum, dmNum)
                .eq(UserDm::getDelflag, DelflagEnum.USABLE.getValue()));
        if (dmCount > 0) {
            throw new BusinessException("该工号已存在，请修改后重试!");
        }

        UserDm entity = new UserDm();
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
