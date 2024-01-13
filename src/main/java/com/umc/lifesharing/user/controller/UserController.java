package com.umc.lifesharing.user.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.config.security.CustomUserDetails;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/auth/login")
    public ApiResponse<UserResponseDTO.ResponseDTO> login(@Valid @RequestBody UserRequestDTO.LoginDTO loginDTO) {
        return ApiResponse.onSuccess(userService.login(loginDTO));
    }


    @PostMapping("/auth/join")
    public ApiResponse<UserResponseDTO.ResponseDTO> join(@Valid @RequestBody UserRequestDTO.JoinDTO joinDTO) {
        return ApiResponse.onSuccess(userService.join(joinDTO));
    }

//    @GetMapping("/qtest")
//    public ApiResponse<?> test(@Valid @AuthenticationPrincipal UserAdapter userAdapter) {
//        log.info("test@@@@@@@@@@@@@@@");
//        log.info(userAdapter.getUsername());
//        log.info(userAdapter.getPassword());
//        return ApiResponse.onSuccess(userAdapter.getUsername());
//    }
}
