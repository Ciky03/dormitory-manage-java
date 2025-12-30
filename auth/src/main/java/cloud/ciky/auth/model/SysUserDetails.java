package cloud.ciky.auth.model;

import cloud.ciky.base.enums.StatusEnum;
import cloud.ciky.system.model.dto.UserAuthDTO;
import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 * 系统用户信息(包含用户名、密码和权限)
 * </p>
 *
 * @author ciky
 * @since 2025/12/11 16:51
 */
@Data
public class SysUserDetails implements UserDetails, CredentialsContainer {

    /**
     * 扩展字段：用户ID
     */
    private String userId;

    /**
     * 用户角色数据权限集合
     */
    private Integer dataScope;

    /**
     * 默认字段
     */
    private String username;
    private String realname;
    private String password;
    private Boolean enabled;
    private Collection<GrantedAuthority> authorities;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private Set<String> perms;

    /**
     * 系统管理用户
     */
    public SysUserDetails(UserAuthDTO user) {
        this.setUserId(user.getUserId());
        this.setUsername(user.getUsername());
        this.setRealname(user.getRealName());
        this.setDataScope(user.getDataScope());
        this.setPassword(user.getPassword());
        this.setEnabled(StatusEnum.ENABLE.getValue().equals(user.getStatus()));
        if (CollUtil.isNotEmpty(user.getRoles())) {
            authorities = user.getRoles().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
        this.setPerms(user.getPerms());
    }

    public SysUserDetails(
            String userId,
            String username,
            String realname,
            String password,
            Integer dataScope,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            Set<? extends GrantedAuthority> authorities
    ) {
        this.userId = userId;
        this.username = username;
        this.realname = realname;
        this.password = password;
        this.dataScope = dataScope;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = Collections.unmodifiableSet(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }


    @Override
    public void eraseCredentials() {
        this.password = null;
    }


    public static void main(String[] args) {
        String password = "111111";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        for (int i = 0; i < 5; i++) {
            //生成密码
            String encode = passwordEncoder.encode(password);
            System.out.println(encode);
            //校验密码,参数1是输入的明文 ，参数2是正确密码加密后的串
            boolean matches = passwordEncoder.matches(password, encode);
            System.out.println(matches);
        }
        boolean matches = passwordEncoder.matches("1234", "$2a$10$fb2RlvFwr9HsRu9vH1OxCu/YiMRw6wy5UI6u3s0A.0bVSuR1UqdHK");
        System.out.println(matches);
    }
}
