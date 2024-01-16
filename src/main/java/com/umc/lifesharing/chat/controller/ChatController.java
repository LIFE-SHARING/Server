package com.umc.lifesharing.chat.controller;

import com.umc.lifesharing.chat.dto.ChatResponseDTO;
import com.umc.lifesharing.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;


    // 미완성
    @PostMapping("/make")
    public ChatResponseDTO.MakeRoomResponseDTO makeRoom(){
        return chatService.makeRoom(0L,1L);
    }
}
