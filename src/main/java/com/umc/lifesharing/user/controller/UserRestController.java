package com.umc.lifesharing.user.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.user.converter.UserConverter;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.service.UserQueryService;
import com.umc.lifesharing.validation.annotation.ExistMembers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
//@RequestMapping("/users")
public class UserRestController {

    private final UserQueryService userQueryService;

    // 회원이 등록한 제품 목록
//    @GetMapping("/{userId}/products")
//    @Operation(summary = "회원이 등록한 제품 조회 API")
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = " "),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//    })
//    @Parameters({
//            @Parameter(name = "memberId", description = "회원의 아이디, path variable 입니다!")
//    })
//    public ApiResponse<UserResponseDTO.ProductPreviewListDTO> getProductList(
//            @ExistMembers @PathVariable(name = "memberId") Long memberId){
//        List<Product> productList = userQueryService.getProductList(memberId);
//        UserResponseDTO.ProductPreviewListDTO productPreviewListDTO = UserConverter.productPreviewListDTO(productList);
//        return ApiResponse.onSuccess(productPreviewListDTO);
//    }
}
