package com.umc.lifesharing.user.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.enums.ProductStatus;
import com.umc.lifesharing.config.security.*;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.s3.AwsS3Service;
import com.umc.lifesharing.user.converter.UserConverter;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.entity.Roles;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.RolesRepository;
import com.umc.lifesharing.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
//    private final JwtUtil jwtUtil;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;
    private final AwsS3Service awsS3Service;

    @Override
    public UserResponseDTO.ResponseDTO join(UserRequestDTO.JoinDTO joinDTO, MultipartFile multipartFile) {
        if(emailDuplicated(joinDTO.getEmail()) || nicknameDuplicated(joinDTO.getName())) {
            throw new UserHandler(ErrorStatus.DUPLICATED_EMAIL_OR_NICKNAME);
        }

        String imageUrl = getImageUrl(multipartFile);
        User user = UserConverter.toUser(joinDTO, passwordEncoder, imageUrl);
        Roles roles = new Roles().addUser(user);

        rolesRepository.save(roles);
        user = userRepository.save(user);

        TokenDTO tokenDTO = jwtProvider.generateTokenByUser(user);

        return UserConverter.toResponseDTO(user, tokenDTO);
    }

    @Override
    public UserResponseDTO.ResponseDTO login(UserRequestDTO.LoginDTO loginDTO) {
        User user = validUserByEmail(loginDTO.getEmail());

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.INVALID_PASSWORD);
        }

        TokenDTO tokenDTO = jwtProvider.generateTokenByUser(user);
        return UserConverter.toResponseDTO(user, tokenDTO);
    }

    @Override
    public UserResponseDTO.ChangePasswordResponseDTO changePassword(UserAdapter userAdapter, UserRequestDTO.ChangePasswordDTO changePasswordDTO) {
        // 변경 감지를 위해..
        User user = userRepository.findByEmail(userAdapter.getUser().getEmail()).get();

        // 비밀번호 불일치
        if(!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new UserHandler(ErrorStatus.INVALID_PASSWORD);
        }

        user.updatePassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);

        return UserResponseDTO.ChangePasswordResponseDTO.builder()
                .isChanged(true)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public String getAdminAuth(UserAdapter userAdapter) {
        try {
            User user = validUserByEmail(userAdapter.getUser().getEmail());
            Roles roles = user.getRoles().get(0);
            roles.toAdmin();
            rolesRepository.save(roles);
            return user.getEmail() + "에 admin 권한 부여 완료";
        } catch (Exception e) {
            throw new UserHandler(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    private User validUserByEmail(String email)  {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));
    }

    private boolean emailDuplicated(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    private boolean nicknameDuplicated(String nickname) {
        return userRepository.existsByName(nickname);
    }

    @Override
    public List<Product> getProductList(UserAdapter userAdapter, String filter) {
        Long userId = userAdapter.getUser().getId();
//        Optional<User> mem = userRepository.findById(userId.getId());

        List<Product> productList = null;

        if (filter.equals("recent")){
            productList = productRepository.findByUserIdOrderByCreatedAtDesc(userId);
        }
        else if (filter.equals("popular")){
            productList = productRepository.findByUserIdOrderByScoreDesc(userId);
        }
        else if (filter.equals("review")){
            productList = productRepository.findByUserIdOrderByReviewCountDesc(userId);
        }

        return productList;
    }


    private String getImageUrl(MultipartFile multipartFile) {
        String url;
        if(!multipartFile.isEmpty())
            url = awsS3Service.uploadUserFiles(Arrays.asList(multipartFile));
        else
            url = "";   // TODO: 기본 image url로 변경
        return url;
    }

//    @Override
//    public NoticeResponse.CreateSuccessDTO createNotice(NoticeRequest.CreateDTO createDTO) {
//    }
}
