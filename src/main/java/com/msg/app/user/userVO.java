package com.msg.app.user;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class userVO implements UserDetails {
    private String username;
    private String password;
    private long id;
    private String name;
    private String email;
    private String phone;
    private Boolean enabled;

    private List<RoleVO> roleVOS;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleVOS;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {

        return UserDetails.super.isAccountNonLocked();
    }
}
