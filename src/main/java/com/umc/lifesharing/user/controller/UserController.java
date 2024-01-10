package com.umc.lifesharing.user.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.service.UserService;
import com.umc.lifesharing.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping
public class UserController {
    private final UserService userService;

    @GetMapping("/auth/login")
    public ApiResponse<?> login(@Valid @RequestBody UserRequestDTO.loginDTO loginDTO) {
        return null;
    }

    @PostMapping("/auth/join")
    public ApiResponse<UserResponseDTO.JoinResponseDTO> join(@Valid @RequestBody UserRequestDTO.JoinDTO joinDTO) {
        return ApiResponse.onSuccess(userService.join(joinDTO));
    }
}
