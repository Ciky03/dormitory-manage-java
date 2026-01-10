package cloud.ciky.base.constant;

import java.util.function.IntFunction;

/**
 * Redis 常量
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
public interface RedisConstants {

    /**
     * 认证模块
     */
    interface Auth {
        String BLACKLIST_TOKEN = "auth:token:blacklist:";  // 黑名单Token
        String JWK_SET = "auth:jwk:set";                 // JWT密钥对
        String LOGIN_TOKEN = "auth:token:login:";          // 登录token
        String REPEAT_TOKEN = "auth:token:repeat:";   // 重复Token
    }

    /**
     * 验证码模块
     */
    interface Captcha {
        String IMAGE_CODE = "captcha:image";              // 图形验证码
        String SMS_LOGIN_CODE = "captcha:sms:login";      // 登录短信验证码
        String SMS_REGISTER_CODE = "captcha:sms:register";// 注册短信验证码
        String MOBILE_CODE = "captcha:mobile";            // 手机验证码（通用）
        String EMAIL_CODE = "captcha:email";              // 邮箱验证码
    }

    /**
     * 微信公众号
     */
    interface WxMp {
        String BIND_TOKEN = "wx:mp:bind:";  //绑定微信
    }


    /**
     * 系统模块
     */
    interface System {
        String CONFIG = "system:config";                 // 系统配置
        String ROLE_PERMS = "system:role:perms"; // 系统角色和权限映射
        String DEPT_PERMS = "system:dept:perms"; // 系统部门和权限映射
        String DEPT_PATH = "system:dept:path";   //系统部门路径
    }

    /**
     * 附件URL
     */
    interface Attach{
        String AVATAR = "attach:url:avatar:";   // 头像附件
    }


    interface Common {
        String REGION_DATA = "common:region";       // 省市区数据
        String ORIGINAL_REGION_DATA = "common:original_region";       // 省市区数据
        String LOCK_PREFIX = "common:lock:"; // 锁前缀
    }


    interface QYWX {
        String ACCESS_TOKEN = "qywx:access_token";
        String USER_INFO = "qywx:userInfo";
    }

    interface Audit {
        String WAIT_AUDIT_1 = "audit:wait:1";       // 等待审批
    }

    interface Employee {
        String JOB_NO_SEQ = "emp:jobno:seq";       // 员工工号序列
    }

    interface Project {
        String PRO_NO_SEQ = "pro:no:seq";       // 项目序列
    }

    interface Customer {
        String CUS_NO_SEQ = "cus:no:seq";       // 客户序列
    }

    interface Supplier {
        String SUP_NO_SEQ = "sup:no:seq";       // 供应商序列
    }

    interface Purchase {
        String PUR_NO_SEQ = "pur:no:seq";       // 采购申请序列
    }

    interface Warehouse {
        String WAREHOUSE_NO_SEQ = "warehouse:no:seq";       // 仓库序列
    }

    interface Process {
        String PROCESS_NO_SEQ = "process:no:seq";       // 流程序列
    }

    interface MessageNotice {
        String REPEAT_NOTICE = "repeat:notice";              // 消息推送重复消息检验
    }
}
