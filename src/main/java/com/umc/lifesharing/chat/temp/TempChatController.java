//package com.umc.lifesharing.chat.temp;
//
//import com.umc.lifesharing.chat.entity.ChatRoom;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping("/tempchat")
//public class TempChatController {
//
//    private final TempChatService service;
//
//    @PostMapping
//    public TempChatRoom createRoom(@RequestParam String name){
//        return service.createRoom(name);
//    }
//
//    @GetMapping
//    public List<TempChatRoom> findAllRooms(){
//        return service.findAllRoom();
//    }
//}
