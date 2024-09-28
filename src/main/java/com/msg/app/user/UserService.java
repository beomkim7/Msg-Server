package com.msg.app.user;

import com.msg.app.user.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public int addUser(UserDTO userDTO) throws Exception {
        userDTO.setPw(passwordEncoder.encode(userDTO.getPw()));
        return userMapper.addUser(userDTO);
    }

    public UserDTO changeName(UserDTO userDTO)throws Exception{
        return userMapper.changeName(userDTO);
    }
}
