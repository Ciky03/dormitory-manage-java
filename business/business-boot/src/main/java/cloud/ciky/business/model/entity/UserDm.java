package cloud.ciky.business.model.entity;

import cloud.ciky.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 宿管表
 * </p>
 *
 * @author ciky
 * @since 2026-03-14 11:40:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_dm")
public class UserDm extends BaseEntity {

    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 宿管工号
     */
    @TableField("dm_num")
    private String dmNum;

    /**
     * 入职日期
     */
    @TableField("entry_date")
    private LocalDate entryDate;

}
