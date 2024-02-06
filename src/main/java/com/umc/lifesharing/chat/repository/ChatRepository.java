package com.umc.lifesharing.chat.repository;

import com.umc.lifesharing.chat.entity.Chat;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByRoomIdOrderByCreatedAtDesc(Long roomId);
    List<Chat> findAllBySenderOrderByCreatedAtDesc(Long senderId);
}