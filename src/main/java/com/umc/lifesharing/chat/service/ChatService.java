package com.umc.lifesharing.chat.service;

import com.umc.lifesharing.chat.dto.ChatDTO;
import com.umc.lifesharing.chat.dto.ChatResponseDTO;
import org.springframework.web.socket.WebSocketSession;

public interface ChatService {
    // 나중에 상품 번호로 채팅하는 기능으로 수정 예정
    ChatResponseDTO.MakeRoomResponseDTO makeRoom(Long sender, Long receiver);
    boolean chatMessage(ChatDTO.ChatMessageDTO messageDTO);
}
