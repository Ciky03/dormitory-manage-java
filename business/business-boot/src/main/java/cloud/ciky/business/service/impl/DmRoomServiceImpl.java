package cloud.ciky.business.service.impl;

import cloud.ciky.base.BasePageQuery;
import cloud.ciky.base.constant.SystemConstants;
import cloud.ciky.base.enums.DelflagEnum;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.base.model.KeyValue;
import cloud.ciky.business.mapper.DmRoomMapper;
import cloud.ciky.business.model.entity.DmRoom;
import cloud.ciky.business.model.form.DmRoomForm;
import cloud.ciky.business.model.query.RoomPageQuery;
import cloud.ciky.business.model.query.RoomTreeQuery;
import cloud.ciky.business.model.vo.DmRoomTreeVO;
import cloud.ciky.business.model.vo.RoomPageVO;
import cloud.ciky.business.service.BuildingDmService;
import cloud.ciky.business.service.DmRoomService;
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
import org.springframework.util.CollectionUtils;

import java.util.Collections;
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
public class DmRoomServiceImpl extends ServiceImpl<DmRoomMapper, DmRoom> implements DmRoomService {

    private final BuildingDmService buildingDmService;
    private final RoomStudentService roomStudentService;

    @Override
    public List<DmRoomTreeVO> listBuildingRoomTree(RoomTreeQuery query) {
        Boolean queryAll = query.getQueryAll();
        String dmId = query.getDmId();
        String studentId = query.getStudentId();

        List<DmRoom> rooms = this.list(new LambdaQueryWrapper<DmRoom>()
                .and(queryAll == null || !queryAll,
                        wrapper -> wrapper.eq(DmRoom::getParentId, SystemConstants.ROOT_NODE_ID))
                .eq(DmRoom::getDelflag, DelflagEnum.USABLE.getValue())
                .orderByAsc(DmRoom::getCreateTime));

        String selectedId = null;
        if (CharSequenceUtil.isNotBlank(studentId)) {
            selectedId = roomStudentService.getSelectedRoomId(studentId);
        }
        if (CharSequenceUtil.isNotBlank(dmId)) {
            selectedId = buildingDmService.getSelectedBuildingId(dmId);
        }

        Set<String> parentIds = rooms.stream()
                .map(DmRoom::getParentId)
                .collect(Collectors.toSet());

        Set<String> roomIds = rooms.stream()
                .map(DmRoom::getId)
                .collect(Collectors.toSet());

        List<String> rootIds = parentIds.stream()
                .filter(id -> !roomIds.contains(id))
                .toList();

        String finalSelectedId = selectedId;
        return rootIds.stream()
                .flatMap(rootId -> buildRoomTree(rootId, rooms, finalSelectedId).stream())
                .toList();
    }

    private List<DmRoomTreeVO> buildRoomTree(String parentId, List<DmRoom> roomList, String selectedId) {
        return CollUtil.emptyIfNull(roomList)
                .stream()
                .filter(room -> room.getParentId().equals(parentId))
                .map(entity -> {
                    DmRoomTreeVO vo = new DmRoomTreeVO();
                    vo.setId(entity.getId());
                    vo.setParentId(entity.getParentId());
                    vo.setTreePath(entity.getTreePath());
                    vo.setRoomNum(entity.getRoomNum());
                    vo.setCapacity(entity.getCapacity());
                    vo.setSelected(Objects.equals(entity.getId(), selectedId));
                    List<DmRoomTreeVO> children = buildRoomTree(entity.getId(), roomList, selectedId);
                    vo.setChildren(children);
                    return vo;
                }).toList();
    }

    @Override
    public Page<RoomPageVO> listRoom(RoomPageQuery query) {
       return this.baseMapper.selectRoom(new Page<>(query.getPageNum(), query.getPageSize()), query);
    }

    @Override
    public DmRoomForm getRoomForm(String id) {
        return this.baseMapper.selectRoomForm(id);
    }

    @Override
    public boolean saveRoom(DmRoomForm form) {
        String id = form.getId();
        String parentId = form.getParentId();
        String roomNum = form.getRoomNum();
        String userId = SecurityUtils.getUserId();

        long count = this.count(new LambdaQueryWrapper<DmRoom>()
                .ne(CharSequenceUtil.isNotBlank(id), DmRoom::getId, id)
                .eq(DmRoom::getParentId, parentId)
                .eq(DmRoom::getRoomNum, roomNum)
                .eq(DmRoom::getDelflag, DelflagEnum.USABLE.getValue()));

        if (count > 0) {
            throw new BusinessException(parentId.equals(SystemConstants.ROOT_NODE_ID) ? "该楼栋已存在，请修改后重试!" : "该宿舍已存在，请修改后重试!");
        }

        DmRoom entity = new DmRoom();
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

        return this.saveOrUpdate(entity);
    }

    private String generateRoomTreePath(String parentId) {
        if (SystemConstants.ROOT_NODE_ID.equals(parentId)) {
            return parentId;
        } else {
            DmRoom parent = this.getById(parentId);
            return parent != null ? parent.getTreePath() + "," + parent.getId() : null;
        }
    }

    @Override
    public boolean deleteRoom(String id) {
        DmRoom entity = this.getById(id);
        if (entity == null) {
            return true;
        }

        long childCount = this.count(new LambdaQueryWrapper<DmRoom>()
                .eq(DmRoom::getParentId, id)
                .eq(DmRoom::getDelflag, DelflagEnum.USABLE.getValue()));
        if (childCount > 0) {
            throw new BusinessException("该楼栋下存在宿舍，无法删除!");
        }

        String userId = SecurityUtils.getUserId();
        return this.update(new LambdaUpdateWrapper<DmRoom>()
                .set(DmRoom::getDelflag, DelflagEnum.REMOVED.getValue())
                .set(DmRoom::getUpdateBy, userId)
                .eq(DmRoom::getId, id));
    }

}
