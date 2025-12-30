package cloud.ciky.file.service.impl;

import cloud.ciky.base.constant.DateFormatConstants;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.file.model.dto.FileDTO;
import cloud.ciky.file.service.OssService;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.IdUtil;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * <p>
 * MinIO 文件上传服务类
 * </p>
 *
 * @author ciky
 * @since 2025/12/10 11:24
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "oss.type", havingValue = "minio")
@ConfigurationProperties(prefix = "oss.minio")
@RequiredArgsConstructor
@Data
public class MinioOssService implements OssService {

    /**
     * 服务Endpoint
     */
    private String endpoint;

    /**
     * 访问凭据
     */
    private String accessKey;

    /**
     * 凭据密钥
     */
    private String secretKey;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 自定义域名
     */
    private String customDomain;

    private MinioClient minioClient;

    // 依赖注入完成之后执行初始化
    @PostConstruct
    public void init() {
        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Override
    public FileDTO uploadFile(MultipartFile file, String bucketName) {
        // 生成文件名(日期文件夹)
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        String uuid = IdUtil.simpleUUID();
        String fileName = DateUtil.format(LocalDateTime.now(), DateFormatConstants.FORMAT7) + '/' + uuid + "." + suffix;
        //  try-with-resource 语法糖自动释放流
        try (InputStream inputStream = file.getInputStream()) {
            // 文件上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .contentType(file.getContentType())
                    .stream(inputStream, inputStream.available(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);

            // 返回文件路径
            String fileUrl;
            // 未配置自定义域名
            if (CharSequenceUtil.isBlank(customDomain)) {
                GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName).object(fileName)
                        .method(Method.GET)
                        .build();

                fileUrl = minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
                fileUrl = fileUrl.substring(0, fileUrl.indexOf("?"));
            } else {
                // 配置自定义文件路径域名
                fileUrl = customDomain + '/' + bucketName + '/' + fileName;
            }

            FileDTO fileDto = new FileDTO();
            fileDto.setName(file.getOriginalFilename());
            fileDto.setBucket(bucketName);
            fileDto.setUrl(fileUrl);
            fileDto.setPath(fileName);
            fileDto.setSize(file.getSize());
            fileDto.setMimeType(FileUtil.getMimeType(fileName));
            try {
                String md5 = DigestUtils.md5Hex(file.getInputStream());
                fileDto.setMd5(md5);
            } catch (IOException e) {
                log.error("文件md5值生成失败:{}", e.getMessage());
                throw new BusinessException("文件md5值生成失败");
            }
            return fileDto;
        } catch (Exception e) {
            log.error("文件上传失败:{}", e.getMessage());
            throw new BusinessException("文件上传失败");
        }
    }

    @Override
    public InputStream downloadFile(String bucket, String path) {
        return getInputStreamFromMinio(bucket, path);
    }

    @Override
    public Boolean deleteFiles(String bucketName, List<String> pathList) {
        if (CollUtil.isEmpty(pathList)) {
            return false;
        }
        List<DeleteObject> objects = pathList.stream()
                .map(DeleteObject::new)
                .toList();

        RemoveObjectsArgs removeObjectsArgs = RemoveObjectsArgs.builder()
                .bucket(bucketName)
                .objects(objects)
                .build();

        Iterable<Result<DeleteError>> results = minioClient.removeObjects(removeObjectsArgs);

        //要想真正删除
        results.forEach(item -> {
            try {
                item.get();
            } catch (Exception e) {
                log.error("删除文件失败,bucket:{},错误信息:{}", bucketName, e.getMessage());
                throw new BusinessException("删除文件失败");
            }
        });

        return true;
    }

    @Override
    public boolean checkFile(String bucket, String path) {
        //数据库存在,再从minio查询
        InputStream inputStream = getInputStreamFromMinio(bucket, path);
        return inputStream != null;
    }

    @Override
    public boolean checkChunk(String fileMd5, Integer chunk, String bucket) {
        //1. 分块存储路径: md5前两位为两个目录
        String object = getChunkFileFolderPath(fileMd5) + chunk;
        //3. 从minio查询
        InputStream inputStream = getInputStreamFromMinio(bucket, object);
        return inputStream != null;
    }

    @Override
    public boolean uploadChunk(MultipartFile file, String fileMd5, Integer chunk, String bucket) {
        //2.分块文件路径
        String chunkFilePath = getChunkFileFolderPath(fileMd5) + chunk;

        //3.上传分块文件到minio
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(chunkFilePath)
                    .contentType(file.getContentType())
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
        } catch (Exception e) {
            log.error("分块文件上传失败:bucket:{},object:{},错误信息:{}", bucket, chunkFilePath, e.getMessage());
            throw new BusinessException("文件上传失败");
        }
        return true;
    }

    @Override
    public FileDTO mergeChunk(String fileMd5, String originName, Integer totalChunk, String bucket) {
        //1. 获取文件分块路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);

        //2.找到所有文件分块
        List<ComposeSource> composeSourceList;
        try {
            composeSourceList = Stream.iterate(0, i -> ++i)
                    .limit(totalChunk)
                    .map(i -> ComposeSource.builder().bucket(bucket).object(chunkFileFolderPath + i).build())
                    .toList();
        } catch (Exception e) {
            throw new BusinessException("文件分块不存在");
        }

        //3. 合并后文件的objectName
        String suffix = FileUtil.getSuffix(originName);
        String object = DateUtil.format(LocalDateTime.now(), DateFormatConstants.FORMAT7) + '/' + fileMd5 + "." + suffix;

        //4. 合并文件
        try {
            minioClient.composeObject(ComposeObjectArgs.builder()
                    .sources(composeSourceList)
                    .bucket(bucket)
                    .object(object)
                    .build());
        } catch (Exception e) {
            log.error("文件合并出错,bucket:{},objectName:{},错误信息:{}", bucket, object, e.getMessage());
            throw new BusinessException("文件合并失败");
        }

        //5. 校验合并后的文件是否一致
        //5.1 获取合并后的文件
        File file = downloadFileFromMinio(bucket, object);
        long fileSize = 0;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            String mergeMd5 = DigestUtils.md5Hex(inputStream);
            //比较原始的md5值和合并后的md5值
            if (!fileMd5.equals(mergeMd5)) {
                log.error("校验合并文件的md5值不一致,原始文件{},合并文件{}", fileMd5, mergeMd5);
                throw new BusinessException("文件校验失败");
            }
            //文件大小
            fileSize = file.length();
        } catch (Exception e) {
            throw new BusinessException("文件校验失败");
        }
        //6. 清理分块文件
        List<String> chunkFileList = IntStream.range(0, totalChunk)
                .mapToObj(i -> chunkFileFolderPath + i)
                .toList();
        deleteFiles(bucket, chunkFileList);

        //7. 文件url
        String fileUrl;
        //未配置自定义域名
        if (CharSequenceUtil.isBlank(customDomain)) {
            //https://<minio-server-endpoint>/<bucket-name>/<object-name>?.....
            try {
                fileUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                        .bucket(bucket)
                        .object(object)
                        .method(Method.GET)
                        .build());
            } catch (Exception e) {
                log.error("获取文件URL失败", e);
                throw new BusinessException("获取文件URL失败");
            }
            fileUrl = fileUrl.substring(0, fileUrl.indexOf("?"));
        } else {
            fileUrl = customDomain + '/' + bucket + '/' + object;
        }

        FileDTO fileDto = new FileDTO();
        fileDto.setMd5(fileMd5);
        fileDto.setName(originName);
        fileDto.setBucket(bucket);
        fileDto.setUrl(fileUrl);
        fileDto.setPath(object);
        fileDto.setSize(fileSize);
        fileDto.setMimeType(FileUtil.getMimeType(originName));
        return fileDto;
    }


    /**
     * <p>
     * 从minio下载文件
     * </p>
     *
     * @param bucket 存储桶
     * @param object 文件对象名
     */
    private File downloadFileFromMinio(String bucket, String object) {
        //临时文件
        File minioFile = null;
        try (InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(object)
                .build())) {
            //创建临时文件
            minioFile = File.createTempFile("minio", ".merge");
            try (FileOutputStream outputStream = new FileOutputStream(minioFile)) {
                IOUtils.copy(stream, outputStream);
            }
            return minioFile;
        } catch (Exception e) {
            log.error("下载文件出错,bucket:{},objectName:{},错误信息:{}", bucket, object, e.getMessage());
            // 可选：删除临时文件
            if (minioFile != null && minioFile.exists()) {
                minioFile.delete();
            }
        }
        throw new BusinessException("下载文件失败");
    }

    /**
     * 获取桶内文件流
     *
     * @param bucket 存储桶名称
     * @param object 存储路径
     */
    private InputStream getInputStreamFromMinio(String bucket, String object) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                    .build());
        } catch (Exception e) {
            log.error("获取文件流失败,bucket:{},objectName:{},错误信息:{}", bucket, object, e.getMessage());
            throw new BusinessException("获取文件流失败");
        }
    }

    /**
     * 得到分块文件的目录
     * date/chunk/0158406d1d5f63c71e7b99f981f63ef7/1
     */
    private String getChunkFileFolderPath(String fileMd5) {
        return DateUtil.format(LocalDateTime.now(), DateFormatConstants.FORMAT7) + "/" + "chunk" + "/" + fileMd5 + "/";
    }

    /**
     * 通过minio获取文件大小
     *
     * @param bucket 存储桶名称
     * @param object 存储路径
     */
    private long getFileSizeFromMinIo(String bucket, String object) {
        StatObjectResponse videoInfo = null;
        try {
            videoInfo = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                    .build());
            return videoInfo.size();
        } catch (Exception e) {
            log.error("获取文件大小失败", e);
            throw new BusinessException("获取文件大小失败");
        }

    }

    /**
     * 创建存储桶(存储桶不存在)
     *
     * @param bucketName 存储桶名称
     */
    @SneakyThrows
    private void createBucketIfAbsent(String bucketName) {
        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build();
        if (!minioClient.bucketExists(bucketExistsArgs)) {
            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build();

            minioClient.makeBucket(makeBucketArgs);

            // 设置存储桶访问权限为PUBLIC， 如果不配置，则新建的存储桶默认是PRIVATE，则存储桶文件会拒绝访问 Access Denied
            SetBucketPolicyArgs setBucketPolicyArgs = SetBucketPolicyArgs
                    .builder()
                    .bucket(bucketName)
                    .config(publicBucketPolicy(bucketName))
                    .build();
            minioClient.setBucketPolicy(setBucketPolicyArgs);
        }
    }

    /**
     * PUBLIC桶策略
     * 如果不配置，则新建的存储桶默认是PRIVATE，则存储桶文件会拒绝访问 Access Denied
     * AWS的S3存储桶策略
     * Principal: 生效用户对象
     * Resource:  指定存储桶
     * Action: 操作行为
     *
     * @param bucketName 存储桶名称
     */
    private static String publicBucketPolicy(String bucketName) {

        return "{\"Version\":\"2012-10-17\","
                + "\"Statement\":[{\"Effect\":\"Allow\","
                + "\"Principal\":{\"AWS\":[\"*\"]},"
                + "\"Action\":[\"s3:ListBucketMultipartUploads\",\"s3:GetBucketLocation\",\"s3:ListBucket\"],"
                + "\"Resource\":[\"arn:aws:s3:::" + bucketName + "\"]},"
                + "{\"Effect\":\"Allow\"," + "\"Principal\":{\"AWS\":[\"*\"]},"
                + "\"Action\":[\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:GetObject\"],"
                + "\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
    }
}
