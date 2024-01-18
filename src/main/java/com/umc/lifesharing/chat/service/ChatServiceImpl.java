package com.umc.lifesharing.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.chat.converter.ChatConverter;
import com.umc.lifesharing.chat.dto.ChatDTO;
import com.umc.lifesharing.chat.dto.ChatResponseDTO;
import com.umc.lifesharing.chat.entity.Chat;
import com.umc.lifesharing.chat.entity.ChatRoom;
import com.umc.lifesharing.chat.repository.ChatRepository;
import com.umc.lifesharing.chat.repository.ChatRoomRepository;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.user.entity.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ProductRepository productRepository;
    private final ChatRepository chatRepository;
    private final ObjectMapper mapper;

    // 추후 상품 번호로 유저를 찾는 것을 구현할 예정
    @Override
    public ChatResponseDTO.MakeRoomResponseDTO makeRoom(Long sender, Long receiver){
        ChatRoom chatRoom = ChatConverter.toChatRoom(sender,receiver);
        chatRoomRepository.save(chatRoom);
        ChatConverter.toMakeRoomResponseDTO(chatRoom);
        return ChatConverter.toMakeRoomResponseDTO(chatRoom);
    }

    // 추후 상품 번호로 유저를 찾는 것을 구현할 예정
    @Override
    public ChatResponseDTO.MakeRoomResponseDTO tempMakeRoom(Long productId, Long sender){
        Long receiver = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.PRODUCT_NOT_FOUND.getMessage())).getUser().getId();
        ChatRoom chatRoom = ChatConverter.toChatRoom(sender,receiver);
        chatRoomRepository.save(chatRoom);
        return ChatConverter.toMakeRoomResponseDTO(chatRoom);
    }


    // 채팅 메세지 저장
    @Override
    public boolean chatMessage(ChatDTO.ChatMessageDTO messageDTO){
        chatRepository.save(ChatConverter.toChat(messageDTO));
        return true;
    }

    // 보내는 사람의 채팅방 리스트
    @Override
    public List<ChatResponseDTO.RoomDetailResponseDTO> roomList(Long sender){
        List<ChatRoom> chatRooms = chatRoomRepository.findAllBySender(sender);
        return ChatConverter.toRoomList(chatRooms);
    }


}
