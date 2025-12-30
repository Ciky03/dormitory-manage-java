package cloud.ciky.file.service.impl;

import cloud.ciky.base.constant.DateFormatConstants;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.file.model.dto.FileDTO;
import cloud.ciky.file.service.OssService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * Aliyun 对象存储服务类
 * </p>
 *
 * @author ciky
 * @since 2025/12/10 11:24
 */
@Component
@ConditionalOnProperty(value = "oss.type", havingValue = "aliyun")
@ConfigurationProperties(prefix = "oss.aliyun")
@RequiredArgsConstructor
@Data
public class AliyunOssService implements OssService {
    /**
     * 服务Endpoint
     */
    private String endpoint;
    /**
     * 访问凭据
     */
    private String accessKeyId;
    /**
     * 凭据密钥
     */
    private String accessKeySecret;
    /**
     * 存储桶名称
     */
    private String bucketName;

    private OSS aliyunOssClient;

    @PostConstruct
    public void init() {
        aliyunOssClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    @Override
    public FileDTO uploadFile(MultipartFile file, String bucket) {
        // 生成文件名(日期文件夹)
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        String uuid = IdUtil.simpleUUID();
        String fileName = DateUtil.format(LocalDateTime.now(), DateFormatConstants.FORMAT7) + '/' + uuid + "." + suffix;
        //  try-with-resource 语法糖自动释放流
        try (InputStream inputStream = file.getInputStream()) {

            // 设置上传文件的元信息，例如Content-Type
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            // 创建PutObjectRequest对象，指定Bucket名称、对象名称和输入流
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, metadata);
            // 上传文件
            aliyunOssClient.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new BusinessException("文件上传失败");
        }
        return null;
    }

    @Override
    public InputStream downloadFile(String bucket, String path) {
        return null;
    }

    @Override
    public Boolean deleteFiles(String bucketName, List<String> filePathList) {
//        Assert.notBlank(filePath, "删除文件路径不能为空");
//        // 文件主机域名
//        String fileHost = "https://" + this.bucketName + "." + endpoint;
//        // +1 是/占一个字符，截断左闭右开
//        String fileName = filePath.substring(fileHost.length() + 1);
//        aliyunOssClient.deleteObject(this.bucketName, fileName);
        return true;
    }

    @Override
    public boolean checkFile(String bucket, String path) {
        return false;
    }

    @Override
    public boolean checkChunk(String fileMd5, Integer chunk, String bucket) {
        return false;
    }

    @Override
    public boolean uploadChunk(MultipartFile file, String fileMd5, Integer chunk, String bucket) {
        return false;
    }

    @Override
    public FileDTO mergeChunk(String fileMd5, String fileName, Integer totalChunk, String bucket) {
        return null;
    }
}
