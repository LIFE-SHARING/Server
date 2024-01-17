//package com.umc.lifesharing.chat.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.umc.lifesharing.chat.temp.TempChatDTO;
//import com.umc.lifesharing.chat.temp.TempChatRoom;
//import com.umc.lifesharing.chat.temp.TempChatService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class WebSocketChatHandler extends TextWebSocketHandler {
//
//    private final ObjectMapper mapper;
//
//    private final TempChatService service;
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        log.info("payload {}", payload);
//
//        TempChatDTO chatMessage = mapper.readValue(payload, TempChatDTO.class);
//        log.info("session {}", chatMessage.toString());
//
//        TempChatRoom room = service.findRoomById(chatMessage.getRoomId());
//        log.info("room {}", room.toString());
//
//        room.handleAction(session, chatMessage, service);
//    }
//}
