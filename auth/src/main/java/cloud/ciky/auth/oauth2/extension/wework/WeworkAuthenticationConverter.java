package cloud.ciky.auth.oauth2.extension.wework;

import cloud.ciky.auth.util.OAuth2EndpointUtils;
import cn.hutool.core.text.CharSequenceUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 微信认证参数解析器
 * 解析请求参数中的微信小程序 Code，并构建相应的身份验证(Authentication)对象
 * </p>
 *
 * @author ciky
 * @since 2025/12/12 12:04
 */
public class WeworkAuthenticationConverter implements AuthenticationConverter {

    public static final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://developer.work.weixin.qq.com/document/path/98151";

    @Override
    public Authentication convert(HttpServletRequest request) {
        // 授权类型 (必需)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!WeworkAuthenticationToken.WEWORK.getValue().equals(grantType)) {
            return null;
        }

        // 客户端信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();

        // 参数提取验证
        MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

        // 令牌申请访问范围验证 (可选)
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) &&
                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.SCOPE,
                    ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        Set<String> requestedScopes = null;
        if (StringUtils.hasText(scope)) {
            requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }

        // 微信小程序 Code (必需)
        String code = parameters.getFirst(OAuth2ParameterNames.CODE);
        if (CharSequenceUtil.isBlank(code)) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    OAuth2ParameterNames.CODE,
                    ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
        // 附加参数(微信小程序 Code)
        Map<String, Object> additionalParameters = parameters
                .entrySet()
                .stream()
                .filter(e ->
                        !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE)
                                && !e.getKey().equals(OAuth2ParameterNames.SCOPE)
                )
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

        return new WeworkAuthenticationToken(
                clientPrincipal,
                requestedScopes,
                additionalParameters
        );
    }


}
