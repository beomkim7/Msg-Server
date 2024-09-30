package com.msg.app.user.DTO;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String id;
    private String pw;
    private String name;
    private String email;
    private Boolean enabled;
    private Long roleId;

    private List<RoleDTO> roleVOs;


}
