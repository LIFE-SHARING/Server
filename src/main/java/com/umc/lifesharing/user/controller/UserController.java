package com.umc.lifesharing.user.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.service.UserQueryService;
import com.umc.lifesharing.user.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
@RequestMapping
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserQueryService userQueryService;

    @PostMapping("/user/login")
    public ApiResponse<UserResponseDTO.ResponseDTO> login(@Valid @RequestBody UserRequestDTO.LoginDTO loginDTO) {
        return ApiResponse.onSuccess(userService.login(loginDTO));
    }


    @PostMapping("/user/join")
    public ApiResponse<UserResponseDTO.ResponseDTO> join(@Valid @RequestBody UserRequestDTO.JoinDTO joinDTO,
                                                         @Nullable @RequestParam(value = "multipartFile") MultipartFile multipartFile) {
        return ApiResponse.onSuccess(userService.join(joinDTO/*, multipartFile*/));
    }

    // 마이 페이지에 진입했을 때
    @GetMapping("/user/my-page")
    public ApiResponse<UserResponseDTO.MyPageResponseDTO> getMyPage(@AuthenticationPrincipal UserAdapter userAdapter) {
        return ApiResponse.onSuccess(userQueryService.getMyPage(userAdapter));
    }

    // 비밀번호 변경
    @PostMapping("/user/password")
    public ApiResponse<UserResponseDTO.ChangePasswordResponseDTO> getMyPage(@AuthenticationPrincipal UserAdapter userAdapter,
                                                   @Valid @RequestBody UserRequestDTO.ChangePasswordDTO changePasswordDTO) {
        return ApiResponse.onSuccess(userService.changePassword(userAdapter, changePasswordDTO));
    }

    // 개인 정보
    @GetMapping("/user/info")
    public ApiResponse<UserResponseDTO.UserInfoResponseDTO> getUserInfo(@AuthenticationPrincipal UserAdapter userAdapter) {
        return ApiResponse.onSuccess(userQueryService.getUserInfo(userAdapter));
    }

//    @GetMapping("/qtest")
//    public ApiResponse<?> test(@Valid @AuthenticationPrincipal UserAdapter userAdapter) {
//        log.info("test@@@@@@@@@@@@@@@");
//        log.info(userAdapter.getUsername());
//        log.info(userAdapter.getPassword());
//        return ApiResponse.onSuccess(userAdapter.getUsername());
//    }
}
