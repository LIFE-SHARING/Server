package com.umc.lifesharing.chat.controller;

import com.umc.lifesharing.chat.dto.ChatDTO;
import com.umc.lifesharing.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class StompChatController {

    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
    private final ChatService chatService;
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatDTO.ChatMessageDTO message){
        message.setMessage(message.getSender() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatDTO.ChatMessageDTO message){
        chatService.chatMessage(message);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}