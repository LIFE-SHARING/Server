package com.umc.lifesharing.user.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.config.security.CustomUserDetails;
import com.umc.lifesharing.config.security.JwtUtil;
import com.umc.lifesharing.config.security.PasswordEncoderConfig;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductStatus;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public UserResponseDTO.ChangePasswordResponseDTO changePassword(UserAdapter userAdapter, UserRequestDTO.ChangePasswordDTO changePasswordDTO) {
        // 변경 감지를 위해..
        User user = userRepository.findByEmail(userAdapter.getUser().getEmail()).get();

        // 비밀번호 불일치
        if(!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.INVALID_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);

        return UserResponseDTO.ChangePasswordResponseDTO.builder()
                .isChanged(true)
                .updatedAt(LocalDateTime.now())
                .build();
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

    @Override
    public List<Product> getProductList(Long memberId) {
        Optional<User> mem = userRepository.findById(memberId);

        return mem.map(member -> {
            // 회원이 가진 Product 중 상태가 "EXIST"인 것만 필터링하여 반환
            return member.getProductList().stream()
                    .filter(product -> ProductStatus.EXIST.equals(product.getProduct_status()))
                    .collect(Collectors.toList());
        }).orElse(Collections.emptyList());
    }
}
