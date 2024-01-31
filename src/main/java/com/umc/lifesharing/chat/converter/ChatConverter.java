package com.umc.lifesharing.chat.converter;

import com.umc.lifesharing.chat.dto.ChatDTO;
import com.umc.lifesharing.chat.dto.ChatResponseDTO;
import com.umc.lifesharing.chat.entity.Chat;
import com.umc.lifesharing.chat.entity.ChatRoom;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChatConverter {
    public static ChatResponseDTO.RoomDetailResponseDTO toRoomDetail(ChatRoom chatRoom){
        return ChatResponseDTO.RoomDetailResponseDTO.builder()
                .roomId(chatRoom.getId())
                .receiverId(chatRoom.getReceiver().getId())
                .senderId(chatRoom.getSender().getId())
                .build();
    }

    public static ChatResponseDTO.RoomDetailDTO toRoomDetailReceiver(ChatRoom chatRoom){
        return ChatResponseDTO.RoomDetailDTO.builder()
                .roomId(chatRoom.getId())
                .receiverId(chatRoom.getReceiver().getId())
                .senderId(chatRoom.getSender().getId())
                .productId(chatRoom.getProduct().getId())
                .opponentImage(chatRoom.getReceiver().getProfileUrl())
                .opponentName(chatRoom.getReceiver().getName())
                .updatedAt(chatRoom.getUpdatedAt())
                .opponentAddress(chatRoom.getReceiver().getLocationList().get(0).getRoadAddress())
                .build();
    }

    public static ChatResponseDTO.RoomDetailDTO toRoomDetailSender(ChatRoom chatRoom){
        return ChatResponseDTO.RoomDetailDTO.builder()
                .roomId(chatRoom.getId())
                .receiverId(chatRoom.getReceiver().getId())
                .senderId(chatRoom.getSender().getId())
                .productId(chatRoom.getProduct().getId())
                .opponentImage(chatRoom.getSender().getProfileUrl())
                .opponentName(chatRoom.getSender().getName())
                .updatedAt(chatRoom.getUpdatedAt())
                .opponentAddress(chatRoom.getSender().getLocationList().get(0).getRoadAddress())
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

    public static ChatRoom toChatRoom(User receiver, User sender, Product product){
        return ChatRoom.builder()
                .receiver(receiver)
                .sender(sender)
                .product(product)
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

    public static List<ChatResponseDTO.RoomDetailDTO> toRoomListTemp(List<ChatRoom> senderChatRooms, List<ChatRoom> receiverChatRooms){
        List<ChatResponseDTO.RoomDetailDTO> senderList = senderChatRooms.stream().map(ChatConverter::toRoomDetailReceiver).toList();
        List<ChatResponseDTO.RoomDetailDTO> receiverList = receiverChatRooms.stream().map(ChatConverter::toRoomDetailReceiver).toList();
        List<ChatResponseDTO.RoomDetailDTO> result = new ArrayList<>();
        result.addAll(senderList);
        result.addAll(receiverList);
        Stream<ChatResponseDTO.RoomDetailDTO> sortedResult = result.stream().sorted(Comparator.comparing(ChatResponseDTO.RoomDetailDTO::getUpdatedAt).reversed());
        return sortedResult.toList();
    }

    public static List<ChatResponseDTO.ChatMessageDTO> toChatList(List<Chat> chats){
        return chats.stream().map(ChatConverter::toChatMessage).toList();
    }
}
