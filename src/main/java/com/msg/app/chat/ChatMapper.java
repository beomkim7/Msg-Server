package com.msg.app.chat;

import com.msg.app.chat.DTO.ChatDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatMapper {

    public int addMsg(ChatDTO chatDTO)throws Exception;
}
