package com.msg.app.user;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class RoleVO {
    private Long roleId;
    private String roleName;

}
