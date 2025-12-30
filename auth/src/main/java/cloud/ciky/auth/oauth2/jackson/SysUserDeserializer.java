package cloud.ciky.auth.oauth2.jackson;

import cloud.ciky.auth.model.SysUserDetails;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.util.Set;

/**
 * <p>
 * 为 {@link User} 类提供的自定义反序列化器。它已在{@link SysUserMixin} 中注册
 * </p>
 *
 * @author ciky
 * @since 2025/12/11 17:49
 */
public class SysUserDeserializer extends JsonDeserializer<SysUserDetails> {

    private static final TypeReference<Set<SimpleGrantedAuthority>> SIMPLE_GRANTED_AUTHORITY_SET = new TypeReference<Set<SimpleGrantedAuthority>>() {
    };

    /**
     * 此方法将创建 {@link User} 对象。即使序列化 JSON 中的密码键为空，它也能确保对象创建成功，
     * 因为可以通过调用 {@link User#eraseCredentials()} 从 {@link User} 中删除凭据。
     * 在这种情况下，序列化 JSON 中将不会包含密码键。
     */
    @Override
    public SysUserDetails deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        Set<? extends GrantedAuthority> authorities = mapper.convertValue(jsonNode.get("authorities"),
                SIMPLE_GRANTED_AUTHORITY_SET);
        JsonNode passwordNode = readJsonNode(jsonNode, "password");
        String userId = readJsonNode(jsonNode, "userId").asText();
        String username = readJsonNode(jsonNode, "username").asText();
        String realname = readJsonNode(jsonNode, "realname").asText();
        String password = passwordNode.asText("");
        Integer dataScope = readJsonNode(jsonNode, "dataScope").asInt();
        boolean enabled = readJsonNode(jsonNode, "enabled").asBoolean();
        boolean accountNonExpired = readJsonNode(jsonNode, "accountNonExpired").asBoolean();
        boolean credentialsNonExpired = readJsonNode(jsonNode, "credentialsNonExpired").asBoolean();
        boolean accountNonLocked = readJsonNode(jsonNode, "accountNonLocked").asBoolean();
        SysUserDetails result = new SysUserDetails(userId, username,realname, password, dataScope, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked,
                authorities);
        if (passwordNode.asText(null) == null) {
            result.eraseCredentials();
        }
        return result;
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

}
