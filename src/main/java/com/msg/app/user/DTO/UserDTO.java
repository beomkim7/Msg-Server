package com.msg.app.user.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements UserDetails {
    private String id;
    private String pw;
    private String name;
    private String email;
    private Boolean enabled;
    private Long roleId;

    private List<RoleDTO> roleVOs;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //GrantedAuthority -> 현재 사용자가 가지고 있는 권한
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(roleVOs == null){
            return authorities;
        } else {
            for(RoleDTO roleVO:this.getRoleVOs()) {
                GrantedAuthority g = new SimpleGrantedAuthority(roleVO.getRoleName());

                log.warn("====== ROLE : {}", g.getAuthority());
                authorities.add(g);
            }
        }
        return authorities;
    }
    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return this.pw;
    }
    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return this.id;
    }
    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }
    @Override
    public boolean isEnabled() {


        return this.enabled;
    }

    public static UserDTO of(String id , String name, String email) {
        return new UserDTO(id, null, name, email, null, null, null);
    }

}
