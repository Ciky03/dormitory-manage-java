package cloud.ciky.business.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 当前宿舍成员视图对象
 *
 * @author ciky
 * @since 2026-03-31 16:40
 */
@Data
@Schema(description = "当前宿舍成员视图对象")
public class RoomMemberVO {

    @Schema(description = "学生id")
    private String studentId;

    @Schema(description = "学生姓名")
    private String studentName;

    @Schema(description = "是否当前登录学生")
    private Boolean isCurrentUser;

    @Schema(description = "头像URL")
    private String avatarUrl;

    @JsonIgnore
    @Schema(hidden = true)
    private String avatarBucket;

    @JsonIgnore
    @Schema(hidden = true)
    private String avatarPath;
}
