package cloud.ciky.business.controller;

import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.result.PageResult;
import cloud.ciky.base.result.Result;
import cloud.ciky.business.model.form.DormitoryForm;
import cloud.ciky.business.model.query.RoomPageQuery;
import cloud.ciky.business.model.query.RoomTreeQuery;
import cloud.ciky.business.model.vo.DormitoryTreeVO;
import cloud.ciky.business.model.vo.RoomPageVO;
import cloud.ciky.business.service.DormitoryService;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.core.annotation.RepeatSubmit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 宿舍表 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2026-03-11 17:04:20
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/dormitory")
public class DormitoryController {

    private final DormitoryService dormitoryService;

    @Operation(summary = "获取楼栋/宿舍树")
    @GetMapping("/tree/list")
    public Result<List<DormitoryTreeVO>> listBuildingRoomTree(@ParameterObject RoomTreeQuery query) {
        List<DormitoryTreeVO> treeList = dormitoryService.listBuildingRoomTree(query);
        return Result.success(treeList);
    }

    @Operation(summary = "获取宿舍分页列表")
    @GetMapping("/list")
    public PageResult<RoomPageVO> listRooms(@ParameterObject RoomPageQuery query) {
        Page<RoomPageVO> roomPage = dormitoryService.listRoom(query);
        return PageResult.success(roomPage);
    }

    @Operation(summary = "获取楼栋/宿舍表单")
    @GetMapping("/form/{id}")
    public Result<DormitoryForm> getRoomForm(@PathVariable String id) {
        DormitoryForm room = dormitoryService.getRoomForm(id);
        return Result.success(room);
    }

    @Operation(summary = "新增楼栋/宿舍")
    @Log(value = "新增楼栋/宿舍", module = LogModuleEnum.DORMITORY)
    @RepeatSubmit
    @PostMapping("/add")
    public Result<Void> addRoom(@Validated @RequestBody DormitoryForm form) {
        boolean result = dormitoryService.saveRoom(form);
        return Result.judge(result);
    }

    @Operation(summary = "编辑楼栋/宿舍")
    @Log(value = "编辑楼栋/宿舍", module = LogModuleEnum.DORMITORY)
    @RepeatSubmit
    @PutMapping("/edit/{id}")
    public Result<Void> editRoom(@Validated @RequestBody DormitoryForm form, @PathVariable String id) {
        form.setId(id);
        boolean result = dormitoryService.saveRoom(form);
        return Result.judge(result);
    }

    @Operation(summary = "删除楼栋/宿舍")
    @Log(value = "删除楼栋/宿舍", module = LogModuleEnum.DORMITORY)
    @DeleteMapping("/del/{id}")
    public Result<Void> deleteRoom(@PathVariable String id) {
        boolean result = dormitoryService.deleteRoom(id);
        return Result.judge(result);
    }
}
