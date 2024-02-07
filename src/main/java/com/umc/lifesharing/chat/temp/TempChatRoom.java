//package com.umc.lifesharing.chat.temp;
//
//import lombok.Builder;
//import lombok.Data;
//import org.springframework.web.socket.WebSocketSession;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Data
//public class TempChatRoom {
//    private String roomId; // 채팅방 아이디
//    private String name; // 채팅방 이름
//    private Set<WebSocketSession> sessions = new HashSet<>();
//
//    @Builder
//    public TempChatRoom(String roomId, String name){
//        this.roomId = roomId;
//        this.name = name;
//    }
//
//    public void handleAction(WebSocketSession session, TempChatDTO message, TempChatService service) {
//
//        if (message.getType().equals(TempChatDTO.MessageType.ENTER)) {
//            // sessions 에 넘어온 session 을 담고,
//            sessions.add(session);
//
//            // message 에는 입장하였다는 메시지를 띄운다
//            message.setMessage(message.getSender() + " 님이 입장하셨습니다");
//            sendMessage(message, service);
//        } else if (message.getType().equals(TempChatDTO.MessageType.TALK)) {
//            message.setMessage(message.getMessage());
//            sendMessage(message, service);
//        }
//    }
//
//    public <T> void sendMessage(T message, TempChatService service) {
//        sessions.parallelStream().forEach(session -> service.sendMessage(session, message));
//    }
//}
