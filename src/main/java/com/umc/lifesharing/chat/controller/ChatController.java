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


    // 미완성
    @PostMapping("/make")
    public ApiResponse<ChatResponseDTO.MakeRoomResponseDTO> makeRoom(){
        log.info("방 만들기 실행");
        return ApiResponse.onSuccess(chatService.makeRoom(0L,1L));
    }

    // 미완성
    @PostMapping("/temp-make/{productId}/{sender}")
    public ApiResponse<ChatResponseDTO.MakeRoomResponseDTO> tempMakeRoom(@PathVariable(name = "productId") Long productId,@PathVariable(name = "sender") Long sender){
        return ApiResponse.onSuccess(chatService.tempMakeRoom(productId,sender));
    }
    @GetMapping("/list/{sender}")
    public ApiResponse<List<ChatResponseDTO.RoomDetailResponseDTO>> roomList(@PathVariable(name = "sender") Long sender){
        return ApiResponse.onSuccess(chatService.roomList(sender));
    }
}
