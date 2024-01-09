package com.umc.lifesharing.user.repository;

import com.umc.lifesharing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Long, User> {
    public User findByEmail(String email);
}
