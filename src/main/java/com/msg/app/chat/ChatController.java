package com.msg.app.chat;

import com.msg.app.chat.DTO.ChatDTO;
import com.msg.app.chat.DTO.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate template;

    @MessageMapping("/chat")
    @SendTo("pub/messages")
    public void sendMsg(ChatDTO chatDTO)throws Exception{
        log.info("{}",chatDTO);

        template.convertAndSend("/pub/messages", chatDTO);
        int result = chatService.msgAdd(chatDTO);
        if(result == 0){

        }
    }
}
