package cloud.ciky.system.filter;

import cloud.ciky.base.result.ResultCode;
import cloud.ciky.security.util.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * <p>
 * feign内部调用认证过滤器
 * (security配置内部调用接口为白名单后, 通过此拦截器指定接口)
 * </p>
 *
 * @author ciky
 * @since 2026-01-11 0:38
 */
@Component
@Slf4j
public class FeignInternalAuthFilter extends OncePerRequestFilter {

    @Value("${internal.token}")
    private String internalToken;

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (matcher.match("/**/internal/**", uri)) {
            String token = request.getHeader("X-Internal-Token");
            if (!internalToken.equals(token)) {
                ResponseUtils.writeErrMsg(response, ResultCode.USER_LOGOUT_AUTHORIZATION_ERROR);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
