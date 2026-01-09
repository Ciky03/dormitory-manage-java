package cloud.ciky.system.service;

import cloud.ciky.file.model.vo.FileVO;
import cloud.ciky.system.model.entity.SysAttach;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 系统附件信息 服务类
 * </p>
 *
 * @author ciky
 * @since 2026-01-09 00:29:09
 */
public interface SysAttachService extends IService<SysAttach> {

    /**
     * <p>
     * 上传附件
     * </p>
     *
     * @author ciky
     * @since 2026/1/9 0:35
     * @param file 文件
     * @param bucket 桶名称
     * @return cloud.ciky.file.model.vo.FileVO
     */
    FileVO uploadAttach(MultipartFile file, String bucket);
}
