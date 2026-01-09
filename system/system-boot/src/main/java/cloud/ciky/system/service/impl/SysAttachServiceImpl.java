package cloud.ciky.system.service.impl;

import cloud.ciky.base.constant.RedisConstants;
import cloud.ciky.base.exception.BusinessException;
import cloud.ciky.file.model.dto.FileDTO;
import cloud.ciky.file.model.vo.FileVO;
import cloud.ciky.file.service.OssService;
import cloud.ciky.security.util.SecurityUtils;
import cloud.ciky.system.enums.AttachBucketEnum;
import cloud.ciky.system.model.entity.SysAttach;
import cloud.ciky.system.mapper.SysAttachMapper;
import cloud.ciky.system.service.SysAttachService;
import cn.hutool.core.annotation.scanner.FieldAnnotationScanner;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 系统附件信息 服务实现类
 * </p>
 *
 * @author ciky
 * @since 2026-01-09 00:29:09
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysAttachServiceImpl extends ServiceImpl<SysAttachMapper, SysAttach> implements SysAttachService {

    private final OssService ossService;
    private final StringRedisTemplate redisTemplate;

    @Override
    public FileVO uploadAttach(MultipartFile file, String bucket) {
        if (CharSequenceUtil.isBlank(bucket)) {
            throw new BusinessException("存储桶名称不能为空");
        }

        FileDTO fileDto = ossService.uploadFile(file, bucket);
        if (fileDto == null) {
            throw new BusinessException("附件上传失败");
        }

        // 保存附件信息
        String optUser = SecurityUtils.getUserId();
        SysAttach entity = new SysAttach();
        entity.setMd5(fileDto.getMd5());
        entity.setName(fileDto.getName());
        entity.setBucket(fileDto.getBucket());
        entity.setPath(fileDto.getPath());
        entity.setSize(fileDto.getSize());
        entity.setMimeType(fileDto.getMimeType());
        entity.setCreateBy(optUser);
        boolean saved = this.save(entity);
        if (!saved) {
            throw new BusinessException("附件信息保存失败");
        }

        // 头像临时URL缓存
        if (bucket.equals(AttachBucketEnum.AVATAR.getValue())) {
            redisTemplate.opsForValue().set(RedisConstants.Attach.AVATAR + entity.getId(), fileDto.getUrl(), 3, TimeUnit.DAYS);
        }

        return new FileVO(entity.getId(), fileDto.getName(), fileDto.getUrl());
    }
}
