package com.msg.app.user;

import com.msg.app.user.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final Set<String> black = new HashSet<>();

    public void addBlack(String token){
        if(token != null){
            black.add(token);
        }
    }

    public boolean addBlackList(String token){
        return black.contains(token);
    }


    public int addUser(UserDTO userDTO) throws Exception {
        userDTO.setPw(passwordEncoder.encode(userDTO.getPw()));
        return userMapper.addUser(userDTO);
    }

    public int changeName(UserDTO userDTO)throws Exception{
        return userMapper.changeName(userDTO);
    }

    public boolean emailValidate(String email) {
        String check = "^\\w([-_.]?\\w)*@\\w([-_.]?\\w)*.[a-zA-Z]+$";

        return email.matches(check);
    }

    public int checkEmail(String email)throws Exception {
        int result = userMapper.checkEmail(email);
        if(result >1) throw new Exception("이미 등록된 이메일");
        return result;
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
