package com.umc.lifesharing.chat.dto;

import lombok.*;

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
    public static class RoomDetailResponseDTO {
        Long roomId;
        Long senderId;
        Long receiverId;
    }

}
