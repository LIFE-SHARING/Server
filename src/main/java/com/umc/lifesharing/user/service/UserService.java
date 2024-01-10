package com.umc.lifesharing.user.service;

import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import org.springframework.stereotype.Service;

public interface UserService {
    public UserResponseDTO.JoinResponseDTO join(UserRequestDTO.JoinDTO joinDTO);
}
