package com.umc.lifesharing.user.service;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.inquiry.dto.InquiryRequestDTO;
import com.umc.lifesharing.inquiry.dto.InquiryResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserResponseDTO.ResponseDTO join(UserRequestDTO.JoinDTO joinDTO, MultipartFile multipartFile);
    UserResponseDTO.ResponseDTO login(UserRequestDTO.LoginDTO loginDTO);

    UserResponseDTO.ChangePasswordResponseDTO changePassword(UserAdapter userAdapter, UserRequestDTO.ChangePasswordDTO changePasswordDTO);

    List<Product> getProductList(UserAdapter userAdapter, String filter);

//    UserResponseDTO.ChangePasswordResponseDTO changeUserInfo(UserAdapter userAdapter, UserRequestDTO.ChangePasswordDTO changePasswordDTO);

//    List<Product> getProductList(Long memberId);
    String getAdminRole(UserAdapter userAdapter);

//    NoticeResponse.CreateSuccessDTO createNotice(UserAdapter userAdapter, NoticeRequest.CreateDTO createDTO);

    // 1. 다른 대여자의 대여물품 리스트로 응답
    UserResponseDTO.ProductPreviewListDTO getOtherProduct(Long userId, UserAdapter userAdapter);

    // 1. 대여자의 물품 리스트 가져오기
    List<Product> getProductsByUserId(Long userId);

    // 2. 대여자의 대여중 물품 리스트 가져오기
    UserResponseDTO.ProductPreviewListDTO getOtherRentProduct(Long userId, UserAdapter userAdapter);

    // 3. 대여자의 리뷰 목록 가져오기
    List<Review> getReviewByProductId(Long productId);
    UserResponseDTO.UserReviewListDTO getOtherReview(Long userId, UserAdapter userAdapter);

    // 대여자 프로필
    UserResponseDTO.UserProfileDTO getOtherProfile(Long userId, UserAdapter userAdapter);
}

