package cloud.ciky.base.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * <p>
 * 日志模块枚举
 * </p>
 *
 * @author ciky
 * @since 2025/12/12 12:15
 */
@Schema(enumAsRef = true)
@Getter
public enum LogModuleEnum {

    EXCEPTION("异常"),
    LOGIN("登录"),
    USER("用户"),
    EMPLOYEE("员工"),
    REGION("区域"),
    DEPT("部门"),
    ROLE("角色"),
    MENU("菜单"),
    DICT("字典"),
    POST("岗位"),
    NOTICE("通知公告"),
    EMAIL("邮箱"),
    PROJECT("项目"),
    PROJECT_TASK("项目任务"),
    FINANCE("财务"),
    CONTRACT("合同"),
    SUBCONTRACT("分包"),
    SUBCONTRACT_CONSULTATION("分包洽商"),
    WAREHOUSE("仓库"),
    WE_ENTRY("入库"),
    WE_OUT("出库"),
    WE_RETURN("退货"),
    WE_ALLOT("调拨"),
    SUPPLIER("供应商"),
    SUPPLIER_TYPE("供应商类型"),
    CUSTOMER("客户"),
    RESOURCE_CATEGORY("资源类别"),
    RESOURCE("资源"),
    PRO_BUDGET("项目预算"),
    PRO_RESOURCE_BUDGET("项目资源预算"),
    PRO_REIMBURSE("项目费用报销"),
    PRO_APPLY("项目出差申请"),
    PRO_BOM("项目BOM"),
    PURCHASE("采购申请"),
    AUDIT("审批"),
    RECRUIT("招聘信息"),
    NEWS("官网新闻"),
    LEAVE_WORD("官网留言"),
    DOC("系统文档"),
    DOC_DIR("系统目录"),
    SETTING("系统配置"),
    COMPANY("公司"),
    OTHER("其他"),
    PROCESS("流程"),
    ISSUE("问题管理"),;

    @JsonValue
    private final String moduleName;

    LogModuleEnum(String moduleName) {
        this.moduleName = moduleName;
    }
}