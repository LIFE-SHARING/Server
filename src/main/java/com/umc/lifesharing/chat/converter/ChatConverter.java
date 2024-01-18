package com.umc.lifesharing.chat.converter;

import com.umc.lifesharing.chat.dto.ChatDTO;
import com.umc.lifesharing.chat.dto.ChatResponseDTO;
import com.umc.lifesharing.chat.entity.Chat;
import com.umc.lifesharing.chat.entity.ChatRoom;

import java.util.List;

public class ChatConverter {
    public static ChatResponseDTO.RoomDetailResponseDTO toRoomDetail(ChatRoom chatRoom){
        return ChatResponseDTO.RoomDetailResponseDTO.builder()
                .roomId(chatRoom.getId())
                .receiverId(chatRoom.getReceiver())
                .senderId(chatRoom.getSender())
                .build();
    }

    public static Chat toChat(ChatDTO.ChatMessageDTO messageDTO){
        return Chat.builder()
                .message(messageDTO.getMessage())
                .sender(messageDTO.getSender())
                .roomId(messageDTO.getRoomId())
                .build();
    }

    //나중에 유저로 변경 예정
    public static ChatRoom toChatRoom(Long receiver, Long sender){
        return ChatRoom.builder()
                .receiver(receiver)
                .sender(sender)
                .build();
    }


    public static ChatResponseDTO.MakeRoomResponseDTO toMakeRoomResponseDTO(ChatRoom chatRoom){
        return ChatResponseDTO.MakeRoomResponseDTO.builder()
                .roomId(chatRoom.getId())
                .senderId(chatRoom.getSender())
                .receiverId(chatRoom.getReceiver())
                .build();
    }

    public static List<ChatResponseDTO.RoomDetailResponseDTO> toRoomList(List<ChatRoom> chatRooms){
        return chatRooms.stream().map(ChatConverter::toRoomDetail).toList();

    }
}
