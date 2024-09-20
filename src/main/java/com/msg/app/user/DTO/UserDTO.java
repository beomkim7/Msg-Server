package com.msg.app.user;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class userVO  {
    private String id;
    private String pw;
    private String name;
    private String email;
    private Boolean enabled;

    private List<RoleVO> roleVOS;


}
