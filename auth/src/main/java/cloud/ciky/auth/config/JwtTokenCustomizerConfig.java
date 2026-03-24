package cloud.ciky.auth.config;

import cloud.ciky.auth.model.SysUserDetails;
import cloud.ciky.base.constant.JwtClaimConstants;
import cloud.ciky.base.constant.SystemConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * JWT 自定义字段配置
 * </p>
 *
 * @author ciky
 * @since 2025/12/11 17:34
 */
@Configuration
public class JwtTokenCustomizerConfig {

    /**
     * JWT 自定义字段
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return context -> {
//            if (context.getAuthorizationGrantType().equals(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
//                Set<String> roles = new HashSet<>();
//                roles.add("ROLE_".concat(SystemConstants.PLATFORM_ROLE_CODE));
//                if (!roles.isEmpty()) {
//                    // 将角色写入 JWT 的 authorities 字段
//                    context.getClaims().claim("authorities", roles);
//                }
//            }
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) && context.getPrincipal() instanceof UsernamePasswordAuthenticationToken) {
                // Customize headers/claims for access_token
                Optional.ofNullable(context.getPrincipal().getPrincipal()).ifPresent(principal -> {
                    JwtClaimsSet.Builder claims = context.getClaims();
                    if (principal instanceof SysUserDetails userDetails) { // 系统用户添加自定义字段

                        claims.claim(JwtClaimConstants.USER_ID, userDetails.getUserId());
                        claims.claim(JwtClaimConstants.BUSINESS_USER_ID, userDetails.getBusinessUserId());
                        claims.claim(JwtClaimConstants.USER_TYPE, userDetails.getUserType());
                        claims.claim(JwtClaimConstants.USERNAME, userDetails.getUsername());
                        claims.claim(JwtClaimConstants.REAL_NAME, userDetails.getRealname());
                        claims.claim(JwtClaimConstants.DATA_SCOPE, Objects.isNull(userDetails.getDataScope())?1:userDetails.getDataScope());
//                        String tenantId = userDetails.getTenantId();
//                        if (StringUtils.isNotBlank(tenantId)) {
//                            claims.claim("tenant_id", tenantId);
//                        }
                        // 这里存入角色至JWT，解析JWT的角色用于鉴权的位置: ResourceServerConfig#jwtAuthenticationConverter
                        var authorities = AuthorityUtils.authorityListToSet(context.getPrincipal().getAuthorities())
                                .stream()
                                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
                        claims.claim(JwtClaimConstants.AUTHORITIES, authorities);

                    }
                });
            }
        };
    }

}
