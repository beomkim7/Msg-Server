package com.msg.app.user;

import com.msg.app.user.DTO.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    @Select("select u.*,r.* from user u join role r on u.roleId=r.id where u.id=#{id}")
    public Optional<UserDTO> getUserById(String id)throws Exception;

    @Select("select * from user")
    public List<UserDTO> getAllUsers()throws Exception;

    @Update("update user set pw=#{pw} where id=#{id}")
    public boolean updateUser(UserDTO userDTO)throws Exception;
}
