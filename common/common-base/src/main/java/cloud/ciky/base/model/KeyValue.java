package cloud.ciky.base.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 下拉选项对象
 * </p>
 *
 * @author ciky
 * @since 2025/12/9 12:03
 */
@Schema(description ="键值对")
@Data
@NoArgsConstructor
public class KeyValue {

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Schema(description="选项的值")
    private String key;

    @Schema(description="选项的标签")
    private String value;

}