package com.umc.lifesharing.chat.service;

import com.umc.lifesharing.chat.dto.ChatDTO;
import com.umc.lifesharing.chat.dto.ChatResponseDTO;
import com.umc.lifesharing.chat.entity.Chat;
import com.umc.lifesharing.user.entity.User;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface ChatService {
    // 상품 번호로 채팅방 만드는 기능
    ChatResponseDTO.MakeRoomResponseDTO makeRoom(Long productId, Long sender);
    boolean chatMessage(ChatDTO.ChatMessageDTO messageDTO);

    // 나중에 실제 유저 데이터와 연동 예정
    List<ChatResponseDTO.RoomDetailDTO> roomList(Long userId);
    List<ChatResponseDTO.RoomDetailTempDTO> roomListTemp(Long userId);

    List<ChatResponseDTO.ChatMessageDTO> chatList(Long roomId);

    boolean chatRoomSenderDelete(Long roomId, Long senderId);
    boolean chatRoomReceiverDelete(Long roomId, Long receiverId);

    boolean chatDelete(Long roodId);
}
