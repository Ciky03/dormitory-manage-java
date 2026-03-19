package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.BuildingDm;
import cloud.ciky.business.mapper.BuildingDmMapper;
import cloud.ciky.business.model.form.BuildingDmForm;
import cloud.ciky.business.service.BuildingDmService;
import cloud.ciky.security.util.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * <p>
 * 宿管楼栋关联表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-19 16:43:49
 */
@Slf4j
@Service
public class BuildingDmServiceImpl extends ServiceImpl<BuildingDmMapper, BuildingDm> implements BuildingDmService {

    @Override
    public String getSelectedBuildingId(String dmId) {
        BuildingDm buildingDm = this.getOne(new LambdaQueryWrapper<BuildingDm>()
                .eq(BuildingDm::getDmId, dmId)
                .eq(BuildingDm::getIsCurrent, true));
        return buildingDm != null ? buildingDm.getBuildingId() : null;
    }

    @Override
    public boolean saveBuildingDm(BuildingDmForm form) {
        String buildingId = form.getBuildingId();
        String dmId = form.getDmId();
        String optUser = SecurityUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();

        // 更改为历史宿管记录
        this.update(new LambdaUpdateWrapper<BuildingDm>()
                .set(BuildingDm::getIsCurrent, false)
                .set(BuildingDm::getEndTime,now)
                .set(BuildingDm::getUpdateBy, optUser)
                .set(BuildingDm::getUpdateTime, now)
                .eq(BuildingDm::getBuildingId, buildingId)
                .eq(BuildingDm::getIsCurrent, true));

        BuildingDm entity = new BuildingDm();
        entity.setBuildingId(buildingId);
        entity.setDmId(dmId);
        entity.setIsCurrent(true);
        entity.setStartTime(now);
        entity.setCreateBy(optUser);
        return this.save(entity);
    }
}
