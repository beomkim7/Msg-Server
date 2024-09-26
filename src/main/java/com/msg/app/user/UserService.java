package com.msg.app.user;

import com.msg.app.user.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public int addUser(UserDTO userDTO) throws Exception {

        return userMapper.addUser(userDTO);
    }
}
