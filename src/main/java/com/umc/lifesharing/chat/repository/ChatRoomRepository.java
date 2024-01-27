package com.umc.lifesharing.chat.repository;

import com.umc.lifesharing.chat.entity.ChatRoom;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllBySender(User sender);
}
