package cloud.ciky.business.service.impl;

import cloud.ciky.business.model.entity.BuildingDm;
import cloud.ciky.business.mapper.BuildingDmMapper;
import cloud.ciky.business.service.BuildingDmService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

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
}
