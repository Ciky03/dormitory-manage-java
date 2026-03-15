package cloud.ciky.business.model.entity;

import cloud.ciky.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 教师表
 * </p>
 *
 * @author ciky
 * @since 2026-03-14 11:40:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_teacher")
public class UserTeacher extends BaseEntity {

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 教师工号
     */
    @TableField("teacher_num")
    private String teacherNum;

    /**
     * 入职日期
     */
    @TableField("entry_date")
    private LocalDate entryDate;

}
