package com.umc.lifesharing.heart.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.heart.converter.HeartConverter;
import com.umc.lifesharing.heart.dto.HeartResponseDTO;
import com.umc.lifesharing.heart.entity.Heart;
import com.umc.lifesharing.heart.service.HeartCommandService;
import com.umc.lifesharing.product.converter.ProductConverter;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.product.service.ProductCommandService;
import com.umc.lifesharing.user.converter.UserConverter;
import com.umc.lifesharing.user.dto.UserResponseDTO;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/heart")
public class HeartRestController {

    private final HeartCommandService heartCommandService;
    private final ProductRepository productRepository;


    @PostMapping("/create")
    @Operation(summary = "좋아요 생성 API")
    public ApiResponse<HeartResponseDTO.createResult> create(@RequestParam Long productId, @AuthenticationPrincipal UserAdapter userAdapter){

        User loggedInUser = userAdapter.getUser();
        Product product = productRepository.findById(productId).get();

        boolean addHeart = heartCommandService.addHeart(loggedInUser, product);

        if (addHeart) {
            // 좋아요 추가가 성공한 경우
            return ApiResponse.onSuccess(HeartConverter.toCreateHeart(new Heart(loggedInUser, product)));
        } else {
            // 이미 좋아요한 경우
            return ApiResponse.onFailure(ErrorStatus.ALREADY_HEART.getCode(), "이미 좋아요한 상품입니다.", null);
        }
    }

    @DeleteMapping("/remove")
    @Operation(summary = "좋아요 삭제 API")
    public ApiResponse<HeartResponseDTO.removeResult> remove(@RequestParam Long productId, @AuthenticationPrincipal UserAdapter userAdapter){

        User loggedInUser = userAdapter.getUser();
        Product product = productRepository.findById(productId).get();

        boolean removeHeart = heartCommandService.removeHeart(loggedInUser, product);

        if (removeHeart){
            return ApiResponse.onSuccess(HeartConverter.toRemoveHeart(new Heart(loggedInUser, product)));
        }
        else {
            return ApiResponse.onFailure(ErrorStatus.ALREADY_REMOVE.getCode(), ErrorStatus.ALREADY_REMOVE.getMessage(), null);
        }
    }

    // 찜한 제품 조회
    @GetMapping("/list")
    @Operation(summary = "찜한 제품 조회 API")
    public ApiResponse<HeartResponseDTO.HeartPreviewListDTO> getLikedList(@AuthenticationPrincipal UserAdapter userAdapter){
        // 로그인한 사용자가 찜한 제품 리스트를 가져오도록 함
        List<Product> productList = heartCommandService.getFavoriteProducts(userAdapter);
        return ApiResponse.onSuccess(HeartConverter.heartPreviewListDTO(productList));
    }

}
