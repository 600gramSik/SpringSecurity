package study.springsecurity.Domain.User.Jwt.UserDetail;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import study.springsecurity.Domain.User.Entity.User;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetail implements UserDetails {
    private final String username;
    private final String password;

    //인가용 생성자
    public CustomUserDetail(User user) {
        username = user.getEmail();
        password = user.getPassword();
    }

    //인증용 생성자
    public CustomUserDetail(String email, String password) {
        this.username = email;
        this.password = password;
    }



    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
