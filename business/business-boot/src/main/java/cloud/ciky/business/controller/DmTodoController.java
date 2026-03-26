package cloud.ciky.business.controller;

import cloud.ciky.base.enums.LogModuleEnum;
import cloud.ciky.base.model.Option;
import cloud.ciky.base.result.PageResult;
import cloud.ciky.base.result.Result;
import cloud.ciky.business.model.form.DmTodoCommentForm;
import cloud.ciky.business.model.form.DmTodoForm;
import cloud.ciky.business.model.query.DmTodoPageQuery;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Todo controller.
 *
 * @author ciky
 * @since 2026-03-26 16:12:54
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/dorm/todo")
public class DmTodoController {

    private final DmTodoService dmTodoService;
    private final DmTodoCommentService dmTodoCommentService;

    @Operation(summary = "Get todo stat")
    @GetMapping("/stat")
    public Result<DmTodoStatVO> getTodoStat() {
        return Result.success(dmTodoService.getTodoStat());
    }

    @Operation(summary = "List todos")
    @GetMapping("/list")
    public PageResult<DmTodoPageVO> listTodo(@RequestParam(required = false) String keywords,
                                             @RequestParam(required = false) Integer status,
                                             @RequestParam(required = false) Integer priority,
                                             @RequestParam(required = false) String assigneeStudentId,
                                             @RequestParam(required = false) Integer dueType,
                                             @RequestParam(defaultValue = "1") Integer pageNum,
                                             @RequestParam(defaultValue = "10") Integer pageSize) {
        DmTodoPageQuery query = new DmTodoPageQuery();
        query.setKeywords(keywords);
        query.setState(status);
        query.setPriority(priority);
        query.setAssigneeStudentId(assigneeStudentId);
        query.setDueType(dueType);
        query.setPageNum(pageNum);
        query.setPageSize(pageSize);
        Page<DmTodoPageVO> page = dmTodoService.listTodo(query);
        return PageResult.success(page);
    }

    @Operation(summary = "Get todo detail")
    @GetMapping("/{id}")
    public Result<DmTodoDetailVO> getTodoDetail(@PathVariable String id) {
        return Result.success(dmTodoService.getTodoDetail(id));
    }

    @Operation(summary = "Add todo")
    @Log(value = "Add dorm todo", module = LogModuleEnum.TODO)
    @RepeatSubmit
    @PostMapping("/add")
    public Result<Void> addTodo(@Validated @RequestBody DmTodoForm form) {
        return Result.judge(dmTodoService.saveTodo(form));
    }

    @Operation(summary = "Edit todo")
    @Log(value = "Edit dorm todo", module = LogModuleEnum.TODO)
    @RepeatSubmit
    @PutMapping("/edit/{id}")
    public Result<Void> editTodo(@PathVariable String id,
                                 @Validated @RequestBody DmTodoForm form) {
        form.setId(id);
        return Result.judge(dmTodoService.saveTodo(form));
    }

    @Operation(summary = "Delete todo")
    @Log(value = "Delete dorm todo", module = LogModuleEnum.TODO)
    @DeleteMapping("/del/{id}")
    public Result<Void> deleteTodo(@PathVariable String id) {
        return Result.judge(dmTodoService.deleteTodo(id));
    }

    @Operation(summary = "Start todo")
    @Log(value = "Start dorm todo", module = LogModuleEnum.TODO)
    @RepeatSubmit
    @PostMapping("/start/{id}")
    public Result<Void> startTodo(@PathVariable String id) {
        return Result.judge(dmTodoService.startTodo(id));
    }

    @Operation(summary = "Complete todo")
    @Log(value = "Complete dorm todo", module = LogModuleEnum.TODO)
    @RepeatSubmit
    @PostMapping("/complete/{id}")
    public Result<Void> completeTodo(@PathVariable String id) {
        return Result.judge(dmTodoService.completeTodo(id));
    }

    @Operation(summary = "Cancel todo")
    @Log(value = "Cancel dorm todo", module = LogModuleEnum.TODO)
    @RepeatSubmit
    @PostMapping("/cancel/{id}")
    public Result<Void> cancelTodo(@PathVariable String id,
                                   @RequestBody DmTodoForm form) {
        return Result.judge(dmTodoService.cancelTodo(id, form.getCancelReason()));
    }

    @Operation(summary = "List todo comments")
    @GetMapping("/comment/list/{todoId}")
    public Result<List<DmTodoCommentVO>> listComment(@PathVariable String todoId) {
        return Result.success(dmTodoCommentService.listComment(todoId));
    }

    @Operation(summary = "Add todo comment")
    @Log(value = "Add dorm todo comment", module = LogModuleEnum.TODO)
    @RepeatSubmit
    @PostMapping("/comment/add")
    public Result<Void> addComment(@Validated @RequestBody DmTodoCommentForm form) {
        return Result.judge(dmTodoCommentService.addComment(form));
    }

    @Operation(summary = "List assignee options")
    @GetMapping("/assignee/options")
    public Result<List<Option<String>>> listAssigneeOptions() {
        return Result.success(dmTodoService.listAssigneeOptions());
    }
}
