package cloud.ciky.business.model.entity;

import cloud.ciky.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 学生表
 * </p>
 *
 * @author ciky
 * @since 2026-03-12 17:53:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_student")
public class UserStudent extends BaseEntity {

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 学号
     */
    @TableField("student_num")
    private String studentNum;

    /**
     * 入学年份
     */
    @TableField("admission_year")
    private Integer admissionYear;

    /**
     * 毕业年份
     */
    @TableField("graduation_year")
    private Integer graduationYear;

}
