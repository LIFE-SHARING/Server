package com.umc.lifesharing.chat.repository;

import com.umc.lifesharing.chat.entity.Chat;
import com.umc.lifesharing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findAllByRoomIdOrderByCreatedAtDesc(Long roomId);
}