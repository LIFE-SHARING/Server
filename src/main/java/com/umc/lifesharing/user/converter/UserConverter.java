package com.umc.lifesharing.user.converter;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.config.security.TokenDTO;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.location.converter.LocationConverter;
import com.umc.lifesharing.location.dto.LocationDTO;
import com.umc.lifesharing.product.entity.ProductImage;
import com.umc.lifesharing.reservation.entity.Reservation;
import com.umc.lifesharing.review.dto.ReviewResponseDTO;
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

      // 회원별 제품 조회 응답 - 대여 물품
    public static UserResponseDTO.ProductPreviewDTO productPreviewDTO(Product product){

        List<String> imageUrls = product.getImages().stream()
                .map(ProductImage::getFullImageUrl)
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
//                .userId(user.getId())
//                .imageUrl(imageUrl)
//                .userName(user.getName())
//                .score(averageScore)   // 모든 제품 별점의 평균
//                .reviewCount(totalReviewCount)   // 어떤 사용자의 제품들에 대한 리뷰 개수
//                .location("사용자로부터 받아오기")  // 28일 - 구현전
//                .productCount(productCnt)
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
                .area(user.getLocationList().stream()
                        .findFirst()
                        .orElseThrow(() -> new UserHandler(ErrorStatus.LOCATION_VALUE_NOT_FOUND))
                        .getDong())
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

    // 대여자 프로필 리뷰 목록 응답
    public static UserResponseDTO.ReviewListDTO otherUserReview(Review review){
                List<String> imageList = review.getImages().stream()
                .map(ReviewImage::getFullImageUrl)
                .collect(Collectors.toList());

//        List<String> imageList = review.getImages().stream()
//                .map(image -> {
//                    String imageUrl = image.getImageUrl(); // 이미지의 상대 경로
//
//                    // 이미지 URL이 상대 경로인 경우에만 baseUrl을 추가
//                    return imageUrl != null && !imageUrl.startsWith("https") ? "https://lifesharing.s3.ap-northeast-2.amazonaws.com/" + imageUrl : imageUrl;
//                })
//                .collect(Collectors.toList());

        // 예약 정보가 없을 경우를 고려하여 미리 초기화
        String totalTime = "";

        // 리뷰에 연결된 예약 정보가 있는 경우에만 totalTime 설정
        Reservation reservation = review.getReservation();
        if (reservation != null) {
            totalTime = reservation.getTotalTime();
        }

        return UserResponseDTO.ReviewListDTO.builder()
                .reviewId(review.getId())
                .userId(review.getUser().getId())
                .nickName(review.getUser().getName())
                .imageList(imageList)
                .score(review.getScore())
                .content(review.getContent())
                .lentDay(totalTime)  // 변경된 부분
                .createdAt(review.getCreatedAt())
                .build();
    }

    public static UserResponseDTO.UserReviewListDTO otherUserReviewListDTO (List<Review> reviewList){
        List<UserResponseDTO.ReviewListDTO> reviewListDTO = reviewList.stream()
                .map(UserConverter::otherUserReview).collect(Collectors.toList());

        return UserResponseDTO.UserReviewListDTO.builder()
//                .userId(user.getId())
//                .userName(user.getName())
//                .imageUrl(imageUrl)
//                .score(averageScore)   // 모든 제품 별점의 평균
//                .reviewCount(totalReviewCount)   // 어떤 사용자의 제품들에 대한 리뷰 개수
//                .location("사용자로부터 받아오기")  // 28일 - 구현전
                .reviewList(reviewListDTO)
                .build();
    }


    // 회원별 제품 조회 응답 - 대여중 물품
    public static UserResponseDTO.ProductPreviewDTO otherRentingProduct(Product product, String rent){

        List<String> imageUrls = product.getImages().stream()
                .map(ProductImage::getFullImageUrl)
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
                .isReserved(rent)
                .build();
    }

    public static UserResponseDTO.ProductPreviewListDTO otherRentingProductList(List<UserResponseDTO.ProductPreviewDTO> productPreViewList){

        return UserResponseDTO.ProductPreviewListDTO.builder()
//                .userId(user.getId())
//                .imageUrl(imageUrl)  // 대여자 프로필 이미지
//                .userName(user.getName())  // 대여자 닉네임
//                .score(averageScore)   // 모든 제품 별점의 평균
//                .reviewCount(totalReviewCount)   // 어떤 사용자의 제품들에 대한 리뷰 개수
//                .location("사용자로부터 받아오기")  // 28일 - 구현전
//                .productCount(productCnt)
                .productList(productPreViewList)
                .build();
    }

    // 대여자 프로필 응답
    public static UserResponseDTO.UserProfileDTO otherUserProfileDTO(User user, Integer averageScore, Integer totalReviewCount, Integer productCnt, Integer rentProductCnt, String imageUrl){
        return UserResponseDTO.UserProfileDTO.builder()
                .userId(user.getId())
                .userName(user.getName())
                .score(averageScore)
                .reviewCount(totalReviewCount)
                .imageUrl(imageUrl)
                .location(null)
                .productCount(productCnt)
                .rentProductCnt(rentProductCnt)
                .build();

    }
}
