package com.umc.lifesharing.user.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.notice.dto.NoticeRequest;
import com.umc.lifesharing.notice.dto.NoticeResponse;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.review.rerpository.ReviewRepository;
import com.umc.lifesharing.user.converter.UserConverter;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.service.UserQueryService;
import com.umc.lifesharing.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserQueryService userQueryService;
    private final ReviewRepository reviewRepository;

    @PostMapping("/user/login")
    @Operation(
            summary = "로그인 API",
            description = "로그인 API 입니다. RequestBody 입니다. 사용자 아이디와 패스워디를 담아주세요"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 에러!",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "loginDTO", description = "로그인에 아이디, 패스워드입니다.")
    })
    public ApiResponse<UserResponseDTO.ResponseDTO> login(@Valid @RequestBody UserRequestDTO.LoginDTO loginDTO) {
        return ApiResponse.onSuccess(userService.login(loginDTO));
    }

    @PostMapping("/user/join")
    @Operation(
            summary = "회원가입 API",
            description = "회원가입 API 입니다. RequestBody입니다. 회원가입에 필요한 정보를 담아주세요"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "access 토큰을 주세요!",content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "joinDTO", description = "회원가입에 필요한 정보들 입니다."),
            @Parameter(name = "multipartFile", description = "사용자의 프로필 이미지입니다. multipartFile입니다.")
    })
    public ApiResponse<UserResponseDTO.ResponseDTO> join(@Valid @RequestPart UserRequestDTO.JoinDTO joinDTO,
                                                         @Nullable @RequestPart(value = "multipartFile") MultipartFile multipartFile) {
        return ApiResponse.onSuccess(userService.join(joinDTO, multipartFile));
    }


    @PostMapping("/user/check-nickname")
    @Operation(
            summary = "닉네임 중복 검사 API",
            description = "닉네임이 중복인지 검사하는 API 입니다. RequestBody입니다. 중복을 확인할 닉네임을 넣어주세요"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "checkNickname", description = "중복을 확읺할 닉네임을 넣어주세요..")
    })
    public ApiResponse<UserResponseDTO.CheckNicknameResponseDTO> existNickname(@RequestBody UserRequestDTO.CheckNickname checkNickname) {
        return ApiResponse.onSuccess(userQueryService.existNickname(checkNickname));
    }

    @GetMapping("/user/my-page")
    @Operation(
            summary = "마이페이지 API",
            description = "마이페이지 진입 시 내 정보를 불러오는 API 입니다. 헤더에 access token 을 넣어주세요!"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 주세요!",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "access 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<UserResponseDTO.MyPageResponseDTO> getMyPage(@AuthenticationPrincipal UserAdapter userAdapter) {
        return ApiResponse.onSuccess(userQueryService.getMyPage(userAdapter));
    }

    // 사용자 비밀번호 정보 변경
    @PatchMapping("/user/password")
    public ApiResponse<UserResponseDTO.ChangePasswordResponseDTO> changeUserPassword(@AuthenticationPrincipal UserAdapter userAdapter,
                                                   @Valid @RequestBody UserRequestDTO.ChangePasswordDTO changePasswordDTO) {
        return ApiResponse.onSuccess(userService.changePassword(userAdapter, changePasswordDTO));
    }

    // 사용자 정보 변경
//    @PatchMapping("/user/info")
//    public ApiResponse<UserResponseDTO.ChangePasswordResponseDTO> changeUserInfo(@AuthenticationPrincipal UserAdapter userAdapter,
//                                                                                 @Valid @RequestBody UserRequestDTO.ChangePasswordDTO changePasswordDTO) {
//        return ApiResponse.onSuccess(userService.changePassword(userAdapter, changePasswordDTO));
//    }

    // 개인 정보 수정페이지 진입 시 개인정보를 가져오는 API
    @GetMapping("/user/info")
    public ApiResponse<UserResponseDTO.UserInfoResponseDTO> getUserInfo(@AuthenticationPrincipal UserAdapter userAdapter) {
        return ApiResponse.onSuccess(userQueryService.getUserInfo(userAdapter));
    }

    @PatchMapping("/user/admin-role")
    public ApiResponse<String> getAdminRole(@AuthenticationPrincipal UserAdapter userAdapter) {
        log.info("getAdminAuth " + userAdapter.getUser().getName());
        return ApiResponse.onSuccess(userService.getAdminAuth(userAdapter));
    }

    // 회원이 등록한 제품 목록
    @GetMapping("/user/products")
    @Operation(summary = "회원이 등록한 제품 조회 API")
    public ApiResponse<UserResponseDTO.ProductPreviewListDTO> getUserProductList(@AuthenticationPrincipal UserAdapter userAdapter){
        // 현재 로그인한 사용자의 정보
        User loggedInUser = userAdapter.getUser();
        List<Product> productList = userService.getProductList(loggedInUser.getId());

        return ApiResponse.onSuccess(UserConverter.productPreviewListDTO(productList));
    }


//    @GetMapping("/user/inquiry")
//    public ApiResponse<UserResponseDTO.InquiryPreviewDTO> getUserInquiry(@AuthenticationPrincipal UserAdapter userAdapter) {
//        return ApiResponse.onSuccess();
//    }

//    @PostMapping("/user/inquiry")
//    public ApiResponse<UserResponseDTO.CreateInquiryDTO> createUserInquiry(@AuthenticationPrincipal UserAdapter userAdapter,
//                                                                              @RequestPart UserRequestDTO.InquiryDTO inquiryDTO,
//                                                                              @Nullable @RequestPart(value = "multipartFile") List<MultipartFile> multipartFile) {
//        return ApiResponse.onSuccess();
//    }
}
