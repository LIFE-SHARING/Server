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
    @MessageMapping(value = "/chat/start")
    public void enter(ChatDTO.ChatStartDTO message){
        message.setMessage(message.getSender() + "님이 " + message.getProductId() + "에 대한 상품 문의를 하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatDTO.ChatMessageDTO message){
        chatService.chatMessage(message);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}