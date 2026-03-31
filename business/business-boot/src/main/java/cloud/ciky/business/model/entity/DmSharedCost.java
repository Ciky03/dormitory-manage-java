package cloud.ciky.business.model.entity;

import cloud.ciky.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 宿舍费用公摊主表
 * </p>
 *
 * @author ciky
 * @since 2026-03-31 12:04:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dm_shared_cost")
public class DmSharedCost extends BaseEntity {

    /**
     * 宿舍id
     */
    @TableField("room_id")
    private String roomId;

    /**
     * 公摊标题
     */
    @TableField("title")
    private String title;

    /**
     * 总金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 发起人学生id
     */
    @TableField("initiator_student_id")
    private String initiatorStudentId;

    /**
     * 费用发生日期
     */
    @TableField("occurred_date")
    private LocalDate occurredDate;

    /**
     * 截止时间
     */
    @TableField("due_time")
    private LocalDateTime dueTime;

    /**
     * 状态 0-草稿 1-已发布 2-已完成 3-已取消
     */
    @TableField("status")
    private Boolean status;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 费用原始凭证附件id
     */
    @TableField("source_voucher_attach_id")
    private String sourceVoucherAttachId;

}
