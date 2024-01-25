package com.umc.lifesharing.chat.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.chat.dto.ChatResponseDTO;
import com.umc.lifesharing.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;

    // 채팅방 만들기
    @PostMapping("/make/{sender}/{productId}")
    public ApiResponse<ChatResponseDTO.MakeRoomResponseDTO> makeRoom(@PathVariable(name = "productId") Long productId,@PathVariable(name = "sender") Long sender){
        return ApiResponse.onSuccess(chatService.makeRoom(productId,sender));
    }

    @GetMapping("/room-list/{sender}")
    public ApiResponse<List<ChatResponseDTO.RoomDetailResponseDTO>> roomList(@PathVariable(name = "sender") Long sender){
        return ApiResponse.onSuccess(chatService.roomList(sender));
    }

    @GetMapping("/chat-list/{chatroom}")
    public ApiResponse<List<ChatResponseDTO.ChatMessageDTO>> chatList(@PathVariable(name = "chatroom") Long chatroom){
        return ApiResponse.onSuccess(chatService.chatList(chatroom));
    }
}
