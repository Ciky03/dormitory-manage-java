package cloud.ciky.system.util;

import cn.hutool.core.util.RandomUtil;

/**
 * <p>
 * 随机密码生成器
 * </p>
 *
 * @author ciky
 * @since 2026-01-08 15:04
 */
public class PasswordGenerator {
    // 定义字符集
    private static final String UPPER_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final String ALL_CHARS = UPPER_CASE + LOWER_CASE + DIGITS + SPECIAL_CHARS;

    public static String generatePassword() {
        StringBuilder password = new StringBuilder();

        // 确保至少有一个大写字母、小写字母和数字
        password.append(RandomUtil.randomChar(UPPER_CASE));
        password.append(RandomUtil.randomChar(LOWER_CASE));
        password.append(RandomUtil.randomChar(SPECIAL_CHARS));

        // 剩余位数随机选择字符（至少8位）
        int remainingLength = 4; // 至少8位，已添加4位，还需至少4位
        for (int i = 0; i < remainingLength; i++) {
            password.append(RandomUtil.randomChar(DIGITS));
        }

        // 打乱顺序以避免固定模式
        return password.toString();
    }

}
