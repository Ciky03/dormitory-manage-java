package cloud.ciky.base.exception;

import cloud.ciky.base.result.IResultCode;
import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

/**
 * <p>
 * 自定义业务异常
 * </p>
 *
 * @author ciky
 * @since 2025-12-9 11:32
 */
@Getter
public class BusinessException extends RuntimeException {

    public IResultCode resultCode;

    public BusinessException(IResultCode errorCode) {
        super(errorCode.getMsg());
        this.resultCode = errorCode;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }


    public BusinessException(String message, Object... args) {
        super(formatMessage(message, args));
    }

    private static String formatMessage(String message, Object... args) {
        return MessageFormatter.arrayFormat(message, args).getMessage();
    }
}
