package com.umc.lifesharing.user.service;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductStatus;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.product.service.ProductQueryService;
import com.umc.lifesharing.product.service.ProductQueryServiceImpl;
import com.umc.lifesharing.user.converter.UserConverter;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername");

        User user = validUserByEmail(email);

        return new UserAdapter(user);
    }

    @Override
    public UserResponseDTO.MyPageResponseDTO getMyPage(UserAdapter userAdapter) {
        log.info("getMyPage");
        User user = userRepository.findByEmail(userAdapter.getUser().getEmail()).get();

        return UserConverter.toMyPageResponseDTO(user);
    }

    @Override
    public UserResponseDTO.UserInfoResponseDTO getUserInfo(UserAdapter userAdapter) {
        log.info("getUserInfo");
        User user = userRepository.findByEmail(userAdapter.getUser().getEmail()).get();

        return UserConverter.toUserInfoResponseDTO(user);
    }

      @Override
    public Optional<User> findByUser(Long id) {
        return userRepository.findById(id);
    }

//    @Override
//    public Boolean checkEmail(String email) {
//        log.info("checkEmail " + email);
//        return isDuplication(email);
//    }

    @Override
    public UserResponseDTO.CheckNicknameResponseDTO existNickname(UserRequestDTO.CheckNickname checkNickname) {
        return UserConverter.toCheckNicknameResponseDTO(existNickname(checkNickname.getNickname()));
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
  
    // email(username)로 user를 찾는 메서드
    private User validUserByEmail(String email) throws UsernameNotFoundException {
        log.info("validUserByEmail");
        User user = userRepository.findByEmail(email).get();
        if(user == null)
            throw new UsernameNotFoundException(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
        return user;
    }

    private boolean existNickname(String nickname) {
        return userRepository.existsByName(nickname);
    }

}
