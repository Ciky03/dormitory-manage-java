package cloud.ciky.system.enums;

import cloud.ciky.base.IBaseEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * <p>
 * 附件桶名称枚举
 * </p>
 *
 * @author ciky
 * @since 2026/1/9 12:17
 */

public enum AttachBucketEnum implements IBaseEnum<String> {

    AVATAR("dm-system-avatar", "系统头像桶");

    @Getter
    private String value;

    @Getter
    private String label;

    AttachBucketEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }


}
