package cloud.ciky.file.service;


import cloud.ciky.file.model.dto.FileDTO;
import cloud.ciky.file.model.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * <p>
 * 对象存储服务接口层
 * </p>
 *
 * @author ciky
 * @since 2025/12/10 11:24
 */
public interface OssService {

    /**
     * 上传文件
     *
     * @param file   表单文件对象
     * @param bucket 存储桶名称
     */
    FileDTO uploadFile(MultipartFile file, String bucket);

    /**
     * <p>
     * 下载文件
     * </p>
     *
     * @param bucket 存储桶名称
     * @param path   文件路径
     */
    InputStream downloadFile(String bucket, String path);

    /**
     * 删除文件
     *
     * @param bucket       存储桶名称
     * @param filePathList 文件path列表
     */
    Boolean deleteFiles(String bucket, List<String> filePathList);

    /**
     * 检查文件
     *
     * @param bucket 桶名称
     * @param path   路径
     */
    boolean checkFile(String bucket, String path);

    /**
     * 检查分块文件
     *
     * @param fileMd5 文件md5值
     * @param chunk   文件分块号
     * @param bucket  存储桶名称
     */
    boolean checkChunk(String fileMd5, Integer chunk, String bucket);

    /**
     * 上传分块文件
     *
     * @param file    文件
     * @param fileMd5 文件md5值
     * @param chunk   文件分块号
     * @param bucket  存储桶名称
     */
    boolean uploadChunk(MultipartFile file, String fileMd5, Integer chunk, String bucket);

    /**
     * 合并分块文件
     *
     * @param fileMd5    文件md5值
     * @param fileName   文件名
     * @param totalChunk 总分块数
     * @param bucket     桶存储名称
     */
    FileDTO mergeChunk(String fileMd5, String fileName, Integer totalChunk, String bucket);

}
