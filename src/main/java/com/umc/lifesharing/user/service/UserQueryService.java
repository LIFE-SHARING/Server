package com.umc.lifesharing.user.service;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserQueryService extends UserDetailsService {
    UserResponseDTO.MyPageResponseDTO getMyPage(UserAdapter userAdapter);
}
