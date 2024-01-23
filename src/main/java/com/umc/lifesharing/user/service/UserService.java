package com.umc.lifesharing.user.service;

import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    UserResponseDTO.ResponseDTO join(UserRequestDTO.JoinDTO joinDTO);
    UserResponseDTO.ResponseDTO login(UserRequestDTO.LoginDTO loginDTO);

    UserResponseDTO.ChangePasswordResponseDTO changePassword(UserAdapter userAdapter, UserRequestDTO.ChangePasswordDTO changePasswordDTO);

    List<Product> getProductList(Long memberId);
}
