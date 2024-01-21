package com.umc.lifesharing.chat.repository;

import com.umc.lifesharing.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllBySender(Long sender);
}
