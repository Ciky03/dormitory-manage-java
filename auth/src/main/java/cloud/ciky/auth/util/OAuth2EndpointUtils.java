package cloud.ciky.auth.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * OAuth2 Endpoint 工具类
 * </p>
 *
 * @author ciky
 * @since 2025/12/11 17:12
 */
public class OAuth2EndpointUtils {

    /**
     * OAuth2 标准错误文档地址，用于返回异常时附带说明
     */
    public static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";


    /**
     * 工具类禁止实例化
     */
    private OAuth2EndpointUtils() {
    }

    /**
     * 将 HttpServletRequest 的参数解析成 MultiValueMap
     * 用于处理 OAuth2 请求中可能存在多个同名参数的情况
     */
    public static MultiValueMap<String, String> getParameters(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
        parameterMap.forEach((key, values) -> {
            for (String value : values) {
                parameters.add(key, value);
            }
        });
        return parameters;
    }

    /**
     * 如果请求符合 Authorization Code 授权模式，则返回请求参数（排除指定字段）
     * 否则返回空 Map
     */
    static Map<String, Object> getParametersIfMatchesAuthorizationCodeGrantRequest(HttpServletRequest request, String... exclusions) {
        if (!matchesAuthorizationCodeGrantRequest(request)) {
            return Collections.emptyMap();
        }
        Map<String, Object> parameters = new HashMap<>(getParameters(request).toSingleValueMap());
        for (String exclusion : exclusions) {
            parameters.remove(exclusion);
        }
        return parameters;
    }


    /**
     * 判断当前请求是否为 OAuth2 授权码模式（grant_type=authorization_code）
     */
    static boolean matchesAuthorizationCodeGrantRequest(HttpServletRequest request) {
        return AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(
                request.getParameter(OAuth2ParameterNames.GRANT_TYPE)) &&
                request.getParameter(OAuth2ParameterNames.CODE) != null;
    }

    /**
     * 判断当前请求是否为 PKCE 模式的授权码 Token 请求（需要额外有 code_verifier 参数）
     */
    static boolean matchesPkceTokenRequest(HttpServletRequest request) {
        return matchesAuthorizationCodeGrantRequest(request) &&
                request.getParameter(PkceParameterNames.CODE_VERIFIER) != null;
    }

    /**
     * 抛出一个标准的 OAuth2AuthenticationException，用于终止请求并返回 OAuth2 错误响应
     */
    public static void throwError(String errorCode, String parameterName, String errorUri) {
        OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
        throw new OAuth2AuthenticationException(error);
    }

    /**
     * 规范化设备授权模式中的 user_code：
     * 1. 转大写
     * 2. 去除非法字符
     * 3. 必须是 8 位字母数字
     * 4. 自动插入中划线：XXXX-XXXX
     */
    static String normalizeUserCode(String userCode) {
        Assert.hasText(userCode, "userCode cannot be empty");
        StringBuilder sb = new StringBuilder(userCode.toUpperCase().replaceAll("[^A-Z\\d]+", ""));
        Assert.isTrue(sb.length() == 8, "userCode must be exactly 8 alpha/numeric characters");
        sb.insert(4, '-');
        return sb.toString();
    }

}
