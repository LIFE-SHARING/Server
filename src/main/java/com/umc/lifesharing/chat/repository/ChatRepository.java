package com.umc.lifesharing.chat.repository;

import com.umc.lifesharing.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}