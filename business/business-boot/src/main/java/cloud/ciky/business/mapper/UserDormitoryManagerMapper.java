package cloud.ciky.business.mapper;

import cloud.ciky.base.BaseQuery;
import cloud.ciky.business.model.entity.UserDormitoryManager;
import cloud.ciky.business.model.vo.DormitoryManagerPageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 宿管表 Mapper 接口
 * </p>
 *
 * @author ciky
 * @since 2026-03-14 11:40:01
 */
@Mapper
public interface UserDormitoryManagerMapper extends BaseMapper<UserDormitoryManager> {

    /**
     * <p>
     * 获取宿管分页列表
     * </p>
     *
     * @author ciky
     * @since 2026/3/16 16:21
     * @param objectPage 分页对象
     * @param query 查询对象
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<cloud.ciky.business.model.vo.DormitoryManagerPageVO>
     */
    Page<DormitoryManagerPageVO> selectDormitoryManagerPage(Page<Object> objectPage, BaseQuery query);
}
