package com.msg.app.user;

import com.msg.app.user.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public int addUser(UserDTO userDTO) throws Exception {
        userDTO.setPw(passwordEncoder.encode(userDTO.getPw()));
        return userMapper.addUser(userDTO);
    }
}
