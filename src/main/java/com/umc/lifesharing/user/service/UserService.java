package com.umc.lifesharing.user.service;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO.ResponseDTO join(UserRequestDTO.JoinDTO joinDTO);
    UserResponseDTO.ResponseDTO login(UserRequestDTO.LoginDTO loginDTO);

    UserResponseDTO.ChangePasswordResponseDTO changePassword(UserAdapter userAdapter, UserRequestDTO.ChangePasswordDTO changePasswordDTO);

    String getAdminAuth(UserAdapter userAdapter);
}