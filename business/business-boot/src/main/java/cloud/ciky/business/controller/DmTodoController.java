package cloud.ciky.business.controller;

import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.model.Option;
import cloud.ciky.base.result.PageResult;
import cloud.ciky.base.result.Result;
import cloud.ciky.business.model.form.DmTodoCommentForm;
import cloud.ciky.business.model.form.DmTodoForm;
import cloud.ciky.business.model.query.DmTodoPageQuery;
import cloud.ciky.business.model.query.StudentPageQuery;
import cloud.ciky.business.model.vo.DmTodoCommentVO;
import cloud.ciky.business.model.vo.DmTodoDetailVO;
import cloud.ciky.business.model.vo.DmTodoPageVO;
import cloud.ciky.business.model.vo.DmTodoStatVO;
import cloud.ciky.business.service.DmTodoCommentService;
import cloud.ciky.business.service.DmTodoService;
import cloud.ciky.core.annotation.Log;
import cloud.ciky.core.annotation.RepeatSubmit;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 宿舍待办事项前端控制器
 * </p>
 *
 * @author ciky
 * @since 2026/3/26 18:45
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/dormitory/todo")
public class DmTodoController {

    private final DmTodoService dmTodoService;
    private final DmTodoCommentService dmTodoCommentService;

    @Operation(summary = "获取宿舍待办统计")
    @GetMapping("/stat")
    public Result<DmTodoStatVO> getTodoStat() {
        return Result.success(dmTodoService.getTodoStat());
    }

    @Operation(summary = "获取宿舍待办分页列表")
    @GetMapping("/list")
    public PageResult<DmTodoPageVO> listTodo( @ParameterObject DmTodoPageQuery query) {
        Page<DmTodoPageVO> page = dmTodoService.listTodo(query);
        return PageResult.success(page);
    }

    @Operation(summary = "获取宿舍待办详情")
    @GetMapping("/detail/{id}")
    public Result<DmTodoDetailVO> getTodoDetail(@PathVariable String id) {
        return Result.success(dmTodoService.getTodoDetail(id));
    }

    @Operation(summary = "新增宿舍待办")
    @Log(value = "新增宿舍待办", module = LogModuleEnum.TODO)
    @RepeatSubmit
    @PostMapping("/add")
    public Result<Void> addTodo(@Validated @RequestBody DmTodoForm form) {
        return Result.judge(dmTodoService.saveTodo(form));
    }

    @Operation(summary = "编辑宿舍待办")
    @Log(value = "编辑宿舍待办", module = LogModuleEnum.TODO)
    @RepeatSubmit
    @PutMapping("/edit/{id}")
    public Result<Void> editTodo(@PathVariable String id,
                                 @Validated @RequestBody DmTodoForm form) {
        form.setId(id);
        return Result.judge(dmTodoService.saveTodo(form));
    }

    @Operation(summary = "删除宿舍待办")
    @Log(value = "删除宿舍待办", module = LogModuleEnum.TODO)
    @DeleteMapping("/del/{id}")
    public Result<Void> deleteTodo(@PathVariable String id) {
        return Result.judge(dmTodoService.deleteTodo(id));
    }

    @Operation(summary = "开始处理宿舍待办")
    @Log(value = "开始处理宿舍待办", module = LogModuleEnum.TODO)
    @RepeatSubmit
    @PostMapping("/start/{id}")
    public Result<Void> startTodo(@PathVariable String id) {
        return Result.judge(dmTodoService.startTodo(id));
    }

    @Operation(summary = "完成宿舍待办")
    @Log(value = "完成宿舍待办", module = LogModuleEnum.TODO)
    @RepeatSubmit
    @PostMapping("/complete/{id}")
    public Result<Void> completeTodo(@PathVariable String id) {
        return Result.judge(dmTodoService.completeTodo(id));
    }

    @Operation(summary = "取消宿舍待办")
    @Log(value = "取消宿舍待办", module = LogModuleEnum.TODO)
    @RepeatSubmit
    @PostMapping("/cancel/{id}")
    public Result<Void> cancelTodo(@PathVariable String id,
                                   @RequestBody DmTodoForm form) {
        return Result.judge(dmTodoService.cancelTodo(id, form.getCancelReason()));
    }

    @Operation(summary = "获取宿舍待办评论列表")
    @GetMapping("/comment/list/{todoId}")
    public Result<List<DmTodoCommentVO>> listComment(@PathVariable String todoId) {
        return Result.success(dmTodoCommentService.listComment(todoId));
    }

    @Operation(summary = "新增宿舍待办评论")
    @Log(value = "新增宿舍待办评论", module = LogModuleEnum.TODO)
    @RepeatSubmit
    @PostMapping("/comment/add")
    public Result<Void> addComment(@Validated @RequestBody DmTodoCommentForm form) {
        return Result.judge(dmTodoCommentService.addComment(form));
    }

    @Operation(summary = "获取当前宿舍负责人候选项")
    @GetMapping("/assignee/options")
    public Result<List<Option<String>>> listAssigneeOptions() {
        return Result.success(dmTodoService.listAssigneeOptions());
    }
}
