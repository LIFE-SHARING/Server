package com.umc.lifesharing.user.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.config.security.CustomUserDetails;
import com.umc.lifesharing.config.security.JwtUtil;
import com.umc.lifesharing.user.converter.UserConverter;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponseDTO.JoinResponseDTO join(UserRequestDTO.JoinDTO joinDTO) {
        if(isDuplicated(joinDTO.getEmail())) {
            throw new UserHandler(ErrorStatus.DUPLICATED_EMAIL);
        }

        User user = UserConverter.toUser(joinDTO);
        user = userRepository.save(user);

        String token = jwtUtil.generateToken(new CustomUserDetails(user));

        return UserConverter.toJoinResponseDTO(user, token);
    }

    public boolean isDuplicated(String email) {
        User user = userRepository.findByEmail(email);

        return user != null;
    }
}
