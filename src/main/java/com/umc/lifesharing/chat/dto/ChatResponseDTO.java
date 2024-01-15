package com.umc.lifesharing.chat.dto;

import lombok.*;

public class ChatResponseDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MakeRoomResponseDTO {
        Long roomId;
        Long senderId;
        Long receiverId;
    }

}
