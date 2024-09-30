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

    public int changeName(UserDTO userDTO)throws Exception{
        return userMapper.changeName(userDTO);
    }

//    //DB 비밀번호 인코딩안됐을때 로직
//    public int updatePassword(UserDTO userDTO) throws Exception {
//        UserDTO existingUser = userMapper.findUserById(userDTO.getId());
//
//        // 비밀번호가 암호화되지 않은 경우에만 업데이트
//        if (existingUser != null && !ispassword(existingUser.getPw())) {
//            existingUser.setPw(passwordEncoder.encode(userDTO.getPw())); // 비밀번호 암호화
//            userMapper.updateUser(existingUser); // 데이터베이스 업데이트
//        }
//        return userMapper.updateUser(existingUser);
//    }
//    private boolean ispassword(String password) {
//        return password!=null && password.length() == 60;
//    }
}
