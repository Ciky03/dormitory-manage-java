package cloud.ciky.system.controller;

import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.base.result.Result;
import cloud.ciky.file.model.vo.FileVO;
import cn.hutool.core.text.CharSequenceUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import cloud.ciky.system.service.SysAttachService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 系统附件信息 前端控制器
 * </p>
 *
 * @author ciky
 * @since 2026-01-09 00:29:09
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/attach")
public class AttachController {

    private final SysAttachService attachService;

    // dm-system-avatar 用户头像桶
    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result<FileVO> uploadFile(
            @Parameter(
                    name = "file",
                    description = "表单文件对象",
                    required = true,
                    in = ParameterIn.DEFAULT,
                    schema = @Schema(name = "file", format = "binary")
            )
            @RequestParam(value = "file") MultipartFile file,
            @Parameter(description = "存储桶名称") @RequestParam(value = "bucket") String bucket
    ) {
        FileVO fileVo = attachService.uploadAttach(file, bucket);
        return Result.success(fileVo);
    }
}
