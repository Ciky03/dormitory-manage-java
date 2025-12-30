package cloud.ciky.base.constant;

import java.util.regex.Pattern;

/**
 * <p>
 * 系统常量
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
public interface SystemConstants {

    /**
     * 根节点ID
     */
    String ROOT_NODE_ID = "0000";

    /**
     * 系统默认密码
     */
    String DEFAULT_PASSWORD = "Zd@123456#$";

    /**
     * 超级管理员角色编码
     */
    String ROOT_ROLE_CODE = "ROOT";
    /**
     * 默认平台角色编码
     */
    String PLATFORM_ROLE_CODE = "PLATFORM";
    /**
     * 供应商平台角色编码
     */
    String SUPPLIER_ROLE_CODE = "SUPPLIER";

    /**
     * 加工厂租户编码
     */
    String TENANT_JGC_CODE = "ZDSB";

    /**
     * 文档管理员角色编码
     */
    String DOC_ADMIN_ROLE_CODE = "DocAdmin";

    /**
     * 密码正则表达式
     * 密码至少包含大小写字母和数字，长度至少为8位
     */
    String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    /**
     * 图片正则表达式
     * 密码至少包含大小写字母和数字，长度至少为8位
     */
    Pattern IMG_PATTERN = Pattern.compile(".*\\.(jpg|jpeg|png|gif|bmp|webp|svg)$", Pattern.CASE_INSENSITIVE);

    /**
     * 身份证正则表达式（含港澳台）
     */
    String ID_NUMBER_REGEX = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|X)$|^[H|M]\\d{8}(?:\\w)?$|^[1|5|7][0-9]{5}(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])[0-9]{3}[\\dX]$|^\\d{8}|^[A-Z0-9]{10}|^\\d{18}$";
}
