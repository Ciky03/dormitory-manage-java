package cloud.ciky.business.model.entity;

import cloud.ciky.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 宿舍费用公摊明细表
 * </p>
 *
 * @author ciky
 * @since 2026-03-31 12:04:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("dm_shared_cost_detail")
public class DmSharedCostDetail extends BaseEntity {

    /**
     * 公摊单id
     */
    @TableField("cost_id")
    private String costId;

    /**
     * 学生id
     */
    @TableField("student_id")
    private String studentId;

    /**
     * 应缴金额
     */
    @TableField("amount_due")
    private BigDecimal amountDue;

    /**
     * 缴费状态 0-未缴 1-已缴
     */
    @TableField("pay_status")
    private Boolean payStatus;

    /**
     * 支付时间
     */
    @TableField("paid_time")
    private LocalDateTime paidTime;

    /**
     * 个人转账凭证附件id
     */
    @TableField("voucher_attach_id")
    private String voucherAttachId;

}
