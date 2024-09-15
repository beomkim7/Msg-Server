package com.msg.app.user;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class RoleVO extends GrantedAuthority {
    private Long roleId;
    private String roleName;

    @Override
    public String getAuthority() {
        return roleName;
    }
}
