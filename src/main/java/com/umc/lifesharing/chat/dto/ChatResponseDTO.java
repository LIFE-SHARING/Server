package com.umc.lifesharing.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

public class ChatResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MakeRoomResponseDTO {
        Long roomId;
        Long senderId;
        Long receiverId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TempMakeRoomResponseDTO {
        Long roomId;
        Long senderId;
        Long productId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomDetailResponseDTO {
        Long roomId;
        Long senderId;
        Long receiverId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomDetailDTO {
        Long roomId;
        Long senderId;
        Long receiverId;
        Long productId;
        String opponentName;
        String opponentImage;
        String opponentAddress;
        String lastMessage;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoomDetailTempDTO {
        Long roomId;
        Long senderId;
        Long receiverId;
        Long productId;
        String opponentName;
        String opponentImage;
        String opponentAddress;
        LocalDateTime updatedAt;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageDTO {
        Long roomId;
        Long senderId;
        String message;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatRoomOutResponseDTO {
        String message;
    }
}
