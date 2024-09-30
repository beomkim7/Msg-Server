package com.msg.app.user;

import com.msg.app.user.DTO.UserDTO;
import org.apache.ibatis.annotations.Mapper;


import java.util.Optional;

@Mapper
public interface UserMapper {


    public Optional<UserDTO> login(String id)throws Exception;
    public int addUser(UserDTO user)throws Exception;
    public UserDTO changeName(UserDTO user)throws Exception;


//    //DB에 있는 암호화되지 않은 비밀번호 암호화
//    public UserDTO findUserById(String id)throws Exception;
//    public int updateUser(UserDTO userDTO)throws Exception;


}
