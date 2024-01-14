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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    // TODO: 비밀번호 변경 추가

    @Override
    public UserResponseDTO.ResponseDTO join(UserRequestDTO.JoinDTO joinDTO) {
        if(isDuplicated(joinDTO.getEmail())) {
            throw new UserHandler(ErrorStatus.DUPLICATED_EMAIL);
        }

        User user = UserConverter.toUser(joinDTO, passwordEncoder);
        user = userRepository.save(user);

        String token = createToken(user);

        return UserConverter.toResponseDTO(user, token);
    }

    @Override
    public UserResponseDTO.ResponseDTO login(UserRequestDTO.LoginDTO loginDTO) {
        User user = validUserByEmail(loginDTO.getEmail());

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.INVALID_PASSWORD);
        }

        String token = createToken(user);

        return UserConverter.toResponseDTO(user, token);
    }

    private String createToken(User user) {
        return jwtUtil.generateToken(new CustomUserDetails(user));
    }

    private User validUserByEmail(String email)  {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));
    }

    private boolean isDuplicated(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
