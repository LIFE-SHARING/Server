package com.umc.lifesharing.chat.repository;

import com.umc.lifesharing.chat.entity.ChatRoom;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllBySender(User sender);
    Optional<ChatRoom> findBySender(User sender);
    Optional<ChatRoom> findByReceiver(User receiver);

}
