package com.umc.lifesharing.user.converter;

import com.umc.lifesharing.config.security.TokenDTO;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.location.converter.LocationConverter;
import com.umc.lifesharing.location.dto.LocationDTO;
import com.umc.lifesharing.product.entity.ProductImage;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.entity.ReviewImage;
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

        List<String> imageUrls = product.getImages().stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList());

        // 이미지 리스트에서 첫 번째 이미지 가져오기
        String firstImageUrl = imageUrls.isEmpty() ? null : imageUrls.get(0);

        return UserResponseDTO.ProductPreviewDTO.builder()
                .productId(product.getId())
                .name(product.getName())
                .imageUrl(firstImageUrl)
                .deposit(product.getDeposit())
                .dayPrice(product.getDayPrice())
                .score(product.getScore()) //별점(평균으로 가져오도록 해야함 - 구현완료)
                .reviewCount(product.getReviewCount()) //리뷰 개수(해당 제품에 대한 리뷰 개수를 카운트해야함 - 구현완료)
                .location("사용자로부터 받아오기")   //위치(구현전-1월 23일)
                .build();
    }

    public static UserResponseDTO.ProductPreviewListDTO productPreviewListDTO(List<Product> productList){
        List<UserResponseDTO.ProductPreviewDTO> productPreViewDTOList = productList.stream()
                .map(UserConverter::productPreviewDTO).collect(Collectors.toList());

        return UserResponseDTO.ProductPreviewListDTO.builder()
                .productList(productPreViewDTOList)
                .build();
    }
  
    public static User toUser(UserRequestDTO.JoinDTO joinDTO, PasswordEncoder passwordEncoder, String imageUrl) {
        return User.builder()
                .email(joinDTO.getEmail())
                .password(passwordEncoder.encode(joinDTO.getPassword()))
                .phone(joinDTO.getPhone())
                .name(joinDTO.getName())
                .profileUrl(imageUrl)
                .build();
    }

    public static UserResponseDTO.UserInfoResponseDTO toUserInfoResponseDTO(User user) {
        return UserResponseDTO.UserInfoResponseDTO.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .nickname(user.getName())
                .locationDTO(LocationConverter.toResponseDTO(user.getLocationList()))
                .profileUrl(user.getProfileUrl())
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
                .dong(user.getLocationList().get(0).getDong())
                .score((int) Math.round(user.getProductList().stream()
                        .mapToInt(Product::getScore)
                        .average()
                        .orElse(0)
                ))
                .nickname(user.getName())
                .profileUrl(user.getProfileUrl())
                .build();
    }

    public static UserResponseDTO.CheckNicknameResponseDTO toCheckNicknameResponseDTO(boolean existNickname) {
        return UserResponseDTO.CheckNicknameResponseDTO.builder()
                .message(existNickname ? "이미 존재하는 닉네임입니다." : "사용할 수 있는 닉네임입니다.")
                .existNickname(existNickname)
                .build();
    }
}
