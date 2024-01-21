package com.umc.lifesharing.chat.service;

import com.umc.lifesharing.chat.dto.ChatDTO;
import com.umc.lifesharing.chat.dto.ChatResponseDTO;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface ChatService {
    // 나중에 상품 번호로 채팅하는 기능으로 수정 예정
    ChatResponseDTO.MakeRoomResponseDTO makeRoom(Long sender, Long receiver);

    // 상품 번호로 채팅방 만드는 기능
    ChatResponseDTO.MakeRoomResponseDTO tempMakeRoom(Long productId, Long sender);
    boolean chatMessage(ChatDTO.ChatMessageDTO messageDTO);

    // 나중에 실제 유저 데이터와 연동 예정
    List<ChatResponseDTO.RoomDetailResponseDTO> roomList(Long sender);
}
