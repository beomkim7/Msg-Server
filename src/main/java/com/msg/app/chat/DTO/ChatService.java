package com.msg.app.chat.DTO;

import com.msg.app.chat.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    @Autowired
    ChatMapper chatMapper;

    public int msgAdd(ChatDTO chatDTO)throws Exception{
        int result = chatMapper.addMsg(chatDTO);
        return result;
    }
}
