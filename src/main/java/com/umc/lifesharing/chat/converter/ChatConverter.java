package com.umc.lifesharing.chat.converter;

import com.umc.lifesharing.chat.dto.ChatResponseDTO;
import com.umc.lifesharing.chat.entity.ChatRoom;

public class ChatConverter {
    public static ChatResponseDTO.RoomDetailResponseDTO toRoomDetail(ChatRoom chatRoom){
        return ChatResponseDTO.RoomDetailResponseDTO.builder()
                .roomId(chatRoom.getId())
                .receiverId(chatRoom.getReceiver())
                .senderId(chatRoom.getSender())
                .build();
    }
}
