package com.umc.lifesharing.user.repository;

import com.umc.lifesharing.user.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Optional<User> findById(Long id);
    Optional<User> findById(Long id);
    //Boolean updateUserById(User user, Long id);
}
