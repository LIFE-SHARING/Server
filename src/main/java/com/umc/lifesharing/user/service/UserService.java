package com.umc.lifesharing.user.service;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.notice.dto.NoticeRequest;
import com.umc.lifesharing.notice.dto.NoticeResponse;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserResponseDTO.ResponseDTO join(UserRequestDTO.JoinDTO joinDTO, MultipartFile multipartFile);
    UserResponseDTO.ResponseDTO login(UserRequestDTO.LoginDTO loginDTO);

    UserResponseDTO.ChangePasswordResponseDTO changePassword(UserAdapter userAdapter, UserRequestDTO.ChangePasswordDTO changePasswordDTO);

//    UserResponseDTO.ChangePasswordResponseDTO changeUserInfo(UserAdapter userAdapter, UserRequestDTO.ChangePasswordDTO changePasswordDTO);

    List<Product> getProductList(Long memberId);
    String getAdminAuth(UserAdapter userAdapter);

//    NoticeResponse.CreateSuccessDTO createNotice(UserAdapter userAdapter, NoticeRequest.CreateDTO createDTO);
}

