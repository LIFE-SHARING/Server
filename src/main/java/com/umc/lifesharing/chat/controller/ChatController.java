package com.umc.lifesharing.chat.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.chat.dto.ChatResponseDTO;
import com.umc.lifesharing.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/list/{sender}")
    public ApiResponse<List<ChatResponseDTO.RoomDetailResponseDTO>> roomList(@PathVariable(name = "sender") Long sender){
        return ApiResponse.onSuccess(chatService.roomList(sender));
    }
}
