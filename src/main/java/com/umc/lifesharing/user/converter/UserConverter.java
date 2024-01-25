package com.umc.lifesharing.user.converter;

import com.umc.lifesharing.config.security.TokenDTO;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.location.dto.LocationDTO;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.entity.enum_class.SocialType;
import org.antlr.v4.runtime.Token;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

public class UserConverter {
//    private PasswordEncoder passwordEncoder;

      // 회원별 제품 조회 응답
    public static UserResponseDTO.ProductPreviewDTO productPreviewDTO(Product product){
        return UserResponseDTO.ProductPreviewDTO.builder()
                .name(product.getName())
                .deposit(product.getDeposit())
                .day_price(product.getDayPrice())
                .score(product.getScore()) //별점(평균으로 가져오도록 해야함 - 구현전)
                .score_count(product.getReviewCount()) //리뷰 개수(해당 제품에 대한 리뷰 개수를 카운트해야함 - 구현전)
                //위치
                .build();
    }

    public static UserResponseDTO.ProductPreviewListDTO productPreviewListDTO(List<Product> productList){
        List<UserResponseDTO.ProductPreviewDTO> productPreViewDTOList = productList.stream()
                .map(UserConverter::productPreviewDTO).collect(Collectors.toList());

        return UserResponseDTO.ProductPreviewListDTO.builder()
                .productList(productPreViewDTOList)
                .build();
    }
  
    public static User toUser(UserRequestDTO.JoinDTO joinDTO, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(joinDTO.getEmail())
                .password(passwordEncoder.encode(joinDTO.getPassword()))
                .phone(joinDTO.getPhone())
                .name(joinDTO.getName())
                .build();
    }

    public static UserResponseDTO.UserInfoResponseDTO toUserInfoResponseDTO(User user) {
        return UserResponseDTO.UserInfoResponseDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .nickname(user.getName())
//                .locationDTO(new LocationDTO(user.getLocationList().get(0)))
                .locationDTO(new LocationDTO("위치 관련 기능 미구현", "위치 관련 기능 미구현", "위치 관련 기능 미구현", "위치 관련 기능 미구현","위치 관련 기능 미구현","위치 관련 기능 미구현"))
                .build();
    }

    public static UserResponseDTO.ResponseDTO toResponseDTO(User user, TokenDTO tokenDTO) {
        return UserResponseDTO.ResponseDTO.builder()
                .userId(user.getId())
                .tokenDTO(tokenDTO)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static UserResponseDTO.MyPageResponseDTO toMyPageResponseDTO(User user) {
        return UserResponseDTO.MyPageResponseDTO.builder()
                .userId(user.getId())
                .point(user.getPoint())
//                .area(user.getLocationList().get(0).getArea())
                .area("위치 관련 기능 미구현")
                .score((int) Math.round(user.getProductList().stream()
                        .mapToInt(Product::getScore)
                        .average()
                        .orElse(0)
                ))
                .nickname(user.getName())
                .imageUrl("이미지 미구현")
                .build();
    }

    public static UserResponseDTO.CheckNicknameResponseDTO toCheckNicknameResponseDTO(boolean existNickname) {
        return UserResponseDTO.CheckNicknameResponseDTO.builder()
                .message(existNickname ? "이미 존재하는 닉네임입니다." : "사용할 수 있는 닉네임입니다.")
                .existNickname(existNickname)
                .build();
    }
}
