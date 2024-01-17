package com.umc.lifesharing.chat.dto;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ChatDTO {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessageDTO {
        private String type;
        private String sender;
        private String roomId;
        private String message;

        public void setSender(String sender) {
            this.sender = sender;
        }

        public void newConnect() {
            this.type = "new";
        }

        public void closeConnect() {
            this.type = "close";
        }

        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ChatRoomDTO {

            private String roomId;
            private String name;
            private Set<WebSocketSession> sessions = new HashSet<>();

            public ChatRoomDTO create(String name) {
                ChatRoomDTO room = new ChatRoomDTO();

                room.roomId = UUID.randomUUID().toString();
                room.name = name;
                return room;
            }
        }
    }
}
