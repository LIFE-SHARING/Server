package com.umc.lifesharing.chat.service;

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
import com.umc.lifesharing.user.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@Slf4j
@Data
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ProductRepository productRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    // 상품 번호로 유저를 찾고, 해당 채팅방 생성
    @Override
    public ChatResponseDTO.MakeRoomResponseDTO makeRoom(Long productId, Long senderId){
        User receiver = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.PRODUCT_NOT_FOUND.getMessage())).getUser();
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOT_FOUNDED.getMessage()));
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
        User user = userRepository.findById(sender)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.USER_NOT_FOUNDED.getMessage()));
        List<ChatRoom> chatRooms = chatRoomRepository.findAllBySender(user);
        return ChatConverter.toRoomList(chatRooms);
    }

    @Override
    public List<ChatResponseDTO.ChatMessageDTO> chatList(Long roomId){
        List<Chat> chats = chatRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId);
        return ChatConverter.toChatList(chats);
    }
}
