package cloud.ciky.security.exception;

import cloud.ciky.base.result.ResultCode;
import cloud.ciky.security.util.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 * 认证失败处理入口
 * </p>
 *
 * @author ciky
 * @since 2025/12/9 14:48
 */
@Component
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 认证失败处理入口方法
     *
     * @param request       触发异常的请求对象（可用于获取请求头、参数等）
     * @param response      响应对象（用于写入错误信息）
     * @param authException 认证异常对象（包含具体失败原因）
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (authException instanceof BadCredentialsException) {
            // 用户名或密码错误
            ResponseUtils.writeErrMsg(response, ResultCode.USERNAME_OR_PASSWORD_ERROR);
        } else if (authException instanceof InsufficientAuthenticationException) {
            // 授权信息不完整-授权失败
            ResponseUtils.writeErrMsg(response, ResultCode.USER_LOGOUT_AUTHORIZATION_ERROR);
        } else {
            // 请求头缺失Authorization、Token格式错误、Token过期、签名验证失败
            ResponseUtils.writeErrMsg(response, ResultCode.TOKEN_INVALID);
        }
    }
}