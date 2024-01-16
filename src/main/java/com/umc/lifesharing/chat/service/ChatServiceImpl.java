package com.umc.lifesharing.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.lifesharing.chat.dto.ChatDTO;
import com.umc.lifesharing.chat.dto.ChatResponseDTO;
import com.umc.lifesharing.chat.entity.Chat;
import com.umc.lifesharing.chat.entity.ChatRoom;
import com.umc.lifesharing.chat.repository.ChatRepository;
import com.umc.lifesharing.chat.repository.ChatRoomRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
@Slf4j
@Data
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ObjectMapper mapper;

    @Override
    public ChatResponseDTO.MakeRoomResponseDTO makeRoom(Long sender, Long receiver){
        ChatRoom chatRoom = ChatRoom.builder()
                .receiver(receiver)
                .sender(sender)
                .build();

        chatRoomRepository.save(chatRoom);

        ChatResponseDTO.MakeRoomResponseDTO result = ChatResponseDTO.MakeRoomResponseDTO.builder()
                .roomId(1L)
                .senderId(sender)
                .receiverId(receiver)
                .build();

        return result;
    }

    @Override
    public boolean chatMessage(ChatDTO.ChatMessageDTO messageDTO){
        Chat chat = Chat.builder()
                .message(messageDTO.getMessage())
                .sender(messageDTO.getSender())
                .roomId(messageDTO.getRoomId())
                .build();
        chatRepository.save(chat);
        return true;
    }
}
