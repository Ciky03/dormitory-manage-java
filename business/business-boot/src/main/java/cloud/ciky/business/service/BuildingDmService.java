package cloud.ciky.business.service;

import cloud.ciky.business.model.entity.BuildingDm;
import cloud.ciky.business.model.form.BuildingDmForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 宿管楼栋关联表 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-03-19 16:43:49
 */
public interface BuildingDmService extends IService<BuildingDm> {

    /**
     * <p>
     * 获取当前宿管负责楼栋
     * </p>
     *
     * @author ciky
     * @since 2026/3/19 17:04
     * @param dmId 宿管id
     * @return java.lang.String
     */
    String getSelectedBuildingId(String dmId);

    /**
     * <p>
     * 保存宿管负责楼栋
     * </p>
     *
     * @author ciky
     * @since 2026/3/19 23:52
     * @param buildingDmForm 表单对象
     * @return boolean
     */
    boolean saveBuildingDm(BuildingDmForm buildingDmForm);
}
