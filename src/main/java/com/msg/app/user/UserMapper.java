package com.msg.app.user;

import com.msg.app.user.DTO.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserMapper {


    public Optional<UserDTO> login(String id)throws Exception;



}
