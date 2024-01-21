package com.umc.lifesharing.chat.dto;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChatDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageDTO{
        private Long sender;
        private Long roomId;
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatStartDTO {
        private Long sender;
        private Long roomId;
        private String message;
        private Long productId;
    }
}
