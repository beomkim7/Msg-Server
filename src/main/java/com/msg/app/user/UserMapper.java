package com.msg.app.user.DTO;

import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    public Optional<UserDTO> getUserById(String id)throws Exception;
}
