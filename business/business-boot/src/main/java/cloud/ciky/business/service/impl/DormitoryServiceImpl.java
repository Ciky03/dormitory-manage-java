package cloud.ciky.business.service.impl;

import cloud.ciky.base.constant.SystemConstants;
import cloud.ciky.base.enums.DelflagEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.business.mapper.DormitoryMapper;
import cloud.ciky.business.model.entity.Dormitory;
import cloud.ciky.business.model.form.BuildingDmForm;
import cloud.ciky.business.model.form.DormitoryForm;
import cloud.ciky.business.model.query.RoomPageQuery;
import cloud.ciky.business.model.query.RoomTreeQuery;
import cloud.ciky.business.model.vo.DormitoryTreeVO;
import cloud.ciky.business.model.vo.RoomPageVO;
import cloud.ciky.business.service.BuildingDmService;
import cloud.ciky.business.service.DormitoryService;
import cloud.ciky.business.service.RoomStudentService;
import cloud.ciky.security.util.SecurityUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 宿舍表 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-03-11 17:04:20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DormitoryServiceImpl extends ServiceImpl<DormitoryMapper, Dormitory> implements DormitoryService {

    private final BuildingDmService buildingDmService;
    private final RoomStudentService roomStudentService;

    @Override
    public List<DormitoryTreeVO> listBuildingRoomTree(RoomTreeQuery query) {
        String dmId = query.getDmId();
        String studentId = query.getStudentId();

        List<DormitoryTreeVO> roomTrees= this.baseMapper.selectBuildingRoomList(query);

        String selectedId = null;
        if (CharSequenceUtil.isNotBlank(studentId)) {
            selectedId = roomStudentService.getSelectedRoomId(studentId);
        }
        if (CharSequenceUtil.isNotBlank(dmId)) {
            selectedId = buildingDmService.getSelectedBuildingId(dmId);
        }

        Set<String> parentIds = roomTrees.stream()
                .map(DormitoryTreeVO::getParentId)
                .collect(Collectors.toSet());

        Set<String> roomIds = roomTrees.stream()
                .map(DormitoryTreeVO::getId)
                .collect(Collectors.toSet());

        List<String> rootIds = parentIds.stream()
                .filter(id -> !roomIds.contains(id))
                .toList();

        String finalSelectedId = selectedId;
        return rootIds.stream()
                .flatMap(rootId -> buildRoomTree(rootId, roomTrees, finalSelectedId).stream())
                .toList();
    }

    private List<DormitoryTreeVO> buildRoomTree(String parentId, List<DormitoryTreeVO> roomList, String selectedId) {
        return CollUtil.emptyIfNull(roomList)
                .stream()
                .filter(room -> room.getParentId().equals(parentId))
                .map(entity -> {
                    entity.setSelected(Objects.equals(entity.getId(), selectedId));
                    List<DormitoryTreeVO> children = buildRoomTree(entity.getId(), roomList, selectedId);
                    entity.setChildren(children);
                    return entity;
                }).toList();
    }

    @Override
    public Page<RoomPageVO> listRoom(RoomPageQuery query) {
       return this.baseMapper.selectRoom(new Page<>(query.getPageNum(), query.getPageSize()), query);
    }

    @Override
    public DormitoryForm getRoomForm(String id) {
        return this.baseMapper.selectRoomForm(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveRoom(DormitoryForm form) {
        String id = form.getId();
        String parentId = form.getParentId();
        String roomNum = form.getRoomNum();
        String userId = SecurityUtils.getUserId();

        long count = this.count(new LambdaQueryWrapper<Dormitory>()
                .ne(CharSequenceUtil.isNotBlank(id), Dormitory::getId, id)
                .eq(Dormitory::getParentId, parentId)
                .eq(Dormitory::getRoomNum, roomNum)
                .eq(Dormitory::getDelflag, DelflagEnum.USABLE.getValue()));

        if (count > 0) {
            throw new BusinessException(parentId.equals(SystemConstants.ROOT_NODE_ID) ? "该楼栋已存在，请修改后重试!" : "该宿舍已存在，请修改后重试!");
        }

        Dormitory entity = new Dormitory();
        if (CharSequenceUtil.isBlank(id)) {
            entity.setCreateBy(userId);
        } else {
            entity.setId(id);
            entity.setUpdateBy(userId);
        }
        entity.setParentId(parentId);
        entity.setRoomNum(roomNum);
        entity.setCapacity(form.getCapacity());
        String treePath = generateRoomTreePath(parentId);
        entity.setTreePath(treePath);

        boolean saved = this.saveOrUpdate(entity);

        if(saved && CharSequenceUtil.isNotBlank(form.getDmId())){
            // 保存宿管负责楼栋
            BuildingDmForm buildingDmForm = new BuildingDmForm();
            buildingDmForm.setBuildingId(entity.getId());
            buildingDmForm.setDmId(form.getDmId());
            saved = buildingDmService.saveBuildingDm(buildingDmForm);
        }
        return saved;

    }

    private String generateRoomTreePath(String parentId) {
        if (SystemConstants.ROOT_NODE_ID.equals(parentId)) {
            return parentId;
        } else {
            Dormitory parent = this.getById(parentId);
            return parent != null ? parent.getTreePath() + "," + parent.getId() : null;
        }
    }

    @Override
    public boolean deleteRoom(String id) {
        Dormitory entity = this.getById(id);
        if (entity == null) {
            return true;
        }

        long childCount = this.count(new LambdaQueryWrapper<Dormitory>()
                .eq(Dormitory::getParentId, id)
                .eq(Dormitory::getDelflag, DelflagEnum.USABLE.getValue()));
        if (childCount > 0) {
            throw new BusinessException("该楼栋下存在宿舍，无法删除!");
        }

        String userId = SecurityUtils.getUserId();
        return this.update(new LambdaUpdateWrapper<Dormitory>()
                .set(Dormitory::getDelflag, DelflagEnum.REMOVED.getValue())
                .set(Dormitory::getUpdateBy, userId)
                .eq(Dormitory::getId, id));
    }

}
