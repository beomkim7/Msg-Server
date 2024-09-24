package com.msg.app.user;

import com.msg.app.user.DTO.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Optional;

@Mapper
public interface UserMapper {

    @Select("select * from user u join role r u.rolenum=r.num where u.id=#{id}")
    public Optional<UserDTO> getUserById(String id)throws Exception;
}
