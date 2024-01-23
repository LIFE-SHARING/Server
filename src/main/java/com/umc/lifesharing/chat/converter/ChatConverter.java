package com.umc.lifesharing.chat.converter;

import com.umc.lifesharing.chat.dto.ChatDTO;
import com.umc.lifesharing.chat.dto.ChatResponseDTO;
import com.umc.lifesharing.chat.entity.Chat;
import com.umc.lifesharing.chat.entity.ChatRoom;
import com.umc.lifesharing.user.entity.User;

import java.util.List;

public class ChatConverter {
    public static ChatResponseDTO.RoomDetailResponseDTO toRoomDetail(ChatRoom chatRoom){
        return ChatResponseDTO.RoomDetailResponseDTO.builder()
                .roomId(chatRoom.getId())
                .receiverId(chatRoom.getReceiver().getId())
                .senderId(chatRoom.getSender().getId())
                .build();
    }

    public static ChatResponseDTO.ChatMessageDTO toChatMessage(Chat chat){
        return ChatResponseDTO.ChatMessageDTO.builder()
                .roomId(chat.getRoomId())
                .senderId(chat.getSender())
                .message(chat.getMessage())
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
    public static ChatRoom toChatRoom(User receiver, User sender){
        return ChatRoom.builder()
                .receiver(receiver)
                .sender(sender)
                .build();
    }

    public static ChatResponseDTO.MakeRoomResponseDTO toMakeRoomResponseDTO(ChatRoom chatRoom){
        return ChatResponseDTO.MakeRoomResponseDTO.builder()
                .roomId(chatRoom.getId())
                .senderId(chatRoom.getSender().getId())
                .receiverId(chatRoom.getReceiver().getId())
                .build();
    }

    public static List<ChatResponseDTO.RoomDetailResponseDTO> toRoomList(List<ChatRoom> chatRooms){
        return chatRooms.stream().map(ChatConverter::toRoomDetail).toList();
    }

    public static List<ChatResponseDTO.ChatMessageDTO> toChatList(List<Chat> chats){
        return chats.stream().map(ChatConverter::toChatMessage).toList();
    }
}
