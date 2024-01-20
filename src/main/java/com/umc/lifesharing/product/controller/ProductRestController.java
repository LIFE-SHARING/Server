package com.umc.lifesharing.product.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.config.security.JwtUtil;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.heart.repository.HeartRepository;
import com.umc.lifesharing.product.converter.ProductConverter;
import com.umc.lifesharing.product.dto.ProductRequestDTO;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.product.service.ProductCommandService;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.service.UserQueryService;
import com.umc.lifesharing.validation.annotation.ExistCategories;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductRestController {

    private final ProductCommandService productCommandService;
    private final ProductRepository productRepository;
    private final UserQueryService userQueryService;
    private final HeartRepository heartRepository;


    // 제품 등록 API
    @PostMapping("/register")
    @Operation(summary = "제품 등록을 위한 API", description = "제품을 등록할 수 있는 API입니다. 회원과 카테고리를 적어주세요.")
    public ApiResponse<ProductResponseDTO.RegisterResultDTO> registerProduct(
            @RequestBody @Valid ProductRequestDTO.RegisterProductDTO request, @AuthenticationPrincipal UserAdapter userAdapter
    ){
        // @AuthenticationPrincipal 어노테이션을 사용하여 현재 로그인한 사용자 정보를 주입받음
        User loggedInUser = userAdapter.getUser();

        // 현재 로그인한 사용자의 아이디를 사용하여 제품을 등록
        Product product = productCommandService.ProductRegister(request, loggedInUser);

        return ApiResponse.onSuccess(ProductConverter.toRegisterResultDTO(product));
    }

    // 제품 삭제 API
    @DeleteMapping("/delete/{productId}")
    @Operation(summary = "제품 삭제 API", description = "해당 제품을 삭제하는 API입니다.")
    @Parameters({
            @Parameter(name = "productId", description = "path variable에 제품 번호를 넣어주세요.")
    })
    public ResponseEntity<String> deleteProduct(@Positive @PathVariable(name = "productId") Long productId,
                                                @AuthenticationPrincipal UserAdapter userAdapter){

        // @AuthenticationPrincipal 어노테이션을 사용하여 현재 로그인한 사용자 정보를 주입받음
        Long loggedInUser = userAdapter.getUser().getId();

        try {
            productCommandService.deleteProduct(productId, loggedInUser);
            return ResponseEntity.ok("성공적으로 삭제되었습니다.");
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body("Product not found.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting product.");
        }
    }

    // 카테고리별 제품 조회 API
    @GetMapping("/category")
    @Operation(summary = "특정 카테고리의 제품 조회 API", description = "해당 카테고리의 제품을 조회하는 API입니다.")
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리 번호를 넣어주세요.")
    })
    public ApiResponse<List<ProductResponseDTO.ProductPreViewDTO>> getProductByCategory(@ExistCategories @RequestParam(name = "categoryId") Long categoryId){

        List<Product> productList = productCommandService.getProductsByCategory(categoryId);

        // 각 제품에 필요한 정보만 남기고 반환
        List<ProductResponseDTO.ProductPreViewDTO> simplifiedProductList = productList.stream()
                .map(ProductConverter::convertToResponseDTO)
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(simplifiedProductList);
    }

    // 제품 상세 조회 API
    @GetMapping("/detail/{productId}")
    @Operation(summary = "제품 상세 조회 API", description = "제품 상세 조회 API입니다. PathVariable에 제품id를 입력하세요.")
    public ApiResponse<ProductResponseDTO.ProductDetailDTO> getProductDetail(@PathVariable(name = "productId") Long productId, @AuthenticationPrincipal UserAdapter userAdapter){
        ProductResponseDTO.ProductDetailDTO productDetail = productCommandService.productDetail(productId, userAdapter);

        return ApiResponse.onSuccess(productDetail);
    }

    // 홈 필터별 제품 조회 API
    @GetMapping("/home")
    @Operation(summary = "홈에서 필터별 제품 조회 API")
    public ApiResponse<List<ProductResponseDTO.ProductPreViewDTO>> getHomeProduct(@RequestParam(name = "filter", defaultValue = "recent") String filter){
        List<Product> productList;

        switch (filter){
            case "popular":
                productList = productRepository.findAllByOrderByScoreDesc();
                break;
            case "review":
                productList = productRepository.findAllByOrderByReviewCountDesc();
                break;
            default:
                productList = productRepository.findAllByOrderByCreatedAtDesc();
        }

        if(productList.isEmpty()){
            return ApiResponse.onFailure(ErrorStatus.PRODUCT_NOT_FOUND.getCode(), "제품이 없습니다.", Collections.emptyList());
        }
        else{
            List<ProductResponseDTO.ProductPreViewDTO> homeProductList = productList.stream()
                    .map(ProductConverter::convertToResponseDTO).collect(Collectors.toList());
            return ApiResponse.onSuccess(homeProductList);
        }
    }

    // 제품 검색 API
    @GetMapping("/search")
    @Operation(summary = "제품 검색 조회 API")

    public ApiResponse<List<ProductResponseDTO.SearchListDTO>> getSearchProduct(@RequestParam(name = "keyword") String keyword,
                                                                                @RequestParam(name = "filter", defaultValue = "recent") String filter,
                                                                                @AuthenticationPrincipal UserAdapter userAdapter){
        List<Product> productList;

        switch (filter){
            case "popular":
                productList = productRepository.findByNameContainingOrderByScoreDesc(keyword);
                break;
            case "review":
                productList = productRepository.findByNameContainingOrderByReviewCountDesc(keyword);
                break;
            default:   // 최신순이 기본값
                productList = productRepository.findByNameContainingOrderByCreatedAtDesc(keyword);
        }

        if(productList.isEmpty()){
            return ApiResponse.onFailure(ErrorStatus.PRODUCT_NOT_FOUND.getCode(), ErrorStatus.PRODUCT_NOT_FOUND.getMessage(), Collections.emptyList());
        }
        else{
            List<ProductResponseDTO.SearchListDTO> searchList = productList.stream()
                    .map(product -> {
                        ProductResponseDTO.SearchListDTO searchListDTO = ProductConverter.searchResultDTO(product);
                        searchListDTO.setIsLiked(productCommandService.isProductLikedByUser(product, userAdapter));
                        return searchListDTO;
                    })
                    .collect(Collectors.toList());
            return ApiResponse.onSuccess(searchList);
        }
    }

}
