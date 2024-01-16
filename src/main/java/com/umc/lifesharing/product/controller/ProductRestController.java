package com.umc.lifesharing.product.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.product.converter.ProductConverter;
import com.umc.lifesharing.product.dto.ProductRequestDTO;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.product.service.ProductCommandService;
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
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductRestController {

    private final ProductCommandService productCommandService;
    private final ProductRepository productRepository;

    // 제품 등록 API
    @PostMapping("/register")
    @Operation(summary = "제품 등록을 위한 API", description = "제품을 등록할 수 있는 API입니다. 회원과 카테고리를 적어주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = " "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<ProductResponseDTO.RegisterResultDTO> registerProduct(
            @RequestBody @Valid ProductRequestDTO.RegisterProductDTO request
    ){
        Product product = productCommandService.ProductRegister(request);
        return ApiResponse.onSuccess(ProductConverter.toRegisterResultDTO(product));
    }

    // 제품 삭제 API
    @DeleteMapping("/{userId}/product/{productId}")
    @Operation(summary = "제품 삭제 API", description = "해당 제품을 삭제하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = " "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "productId", description = "path variable에 제품 번호를 넣어주세요.")
    })
    public ResponseEntity<String> deleteProduct(@PathVariable(name = "userId") Long userId,
                                                @Positive @PathVariable(name = "productId") Long productId){
        try {
            productCommandService.deleteProduct(productId, userId);
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
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = " "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리 번호를 넣어주세요.")
    })
    public ResponseEntity<List<ProductResponseDTO.ProductPreViewDTO>> getProductByCategory(@ExistCategories @RequestParam(name = "categoryId") Long categoryId){
        List<Product> productList = productCommandService.getProductsByCategory(categoryId);

        // 각 제품에 필요한 정보만 남기고 반환
        List<ProductResponseDTO.ProductPreViewDTO> simplifiedProductList = productList.stream()
                .map(ProductConverter::convertToResponseDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(simplifiedProductList, HttpStatus.OK);
    }

    // 제품 상세 조회 API
    @GetMapping("/{productId}")
    @Operation(summary = "제품 상세 조회 API", description = "제품 상세 조회 API입니다. PathVariable에 제품id를 입력하세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = " "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<ProductResponseDTO.ProductDetailDTO> getProductDetail(@PathVariable(name = "productId") Long productId){
        ProductResponseDTO.ProductDetailDTO productDetail = productCommandService.productDetail(productId);
        return ApiResponse.onSuccess(productDetail);
    }

    // 최신순 제품 조회 API
    @GetMapping("/recent")
    @Operation(summary = "최신순 제품 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = " "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<List<ProductResponseDTO.ProductPreViewDTO>> getRecentProduct(){
        List<Product> productList = productRepository.findAllByOrderByCreatedAtDesc();

        List<ProductResponseDTO.ProductPreViewDTO> productPreViewDTOList = productList.stream()
                .map(ProductConverter::convertToResponseDTO)
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(productPreViewDTOList);
    }

    // 인기순 제품 조회 API
    @GetMapping("/popular")
    @Operation(summary = "인기순 제품 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = " "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<List<ProductResponseDTO.ProductPreViewDTO>> getPopularProduct(){
        List<Product> productList = productRepository.findAllByOrderByScoreDesc();

        List<ProductResponseDTO.ProductPreViewDTO> productPreViewDTOList = productList.stream()
                .map(ProductConverter::convertToResponseDTO)
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(productPreViewDTOList);
    }

    // 리뷰순 제품 조회 API
    @GetMapping("/review")
    @Operation(summary = "리뷰순 제품 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = " "),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "acess 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH006", description = "acess 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<List<ProductResponseDTO.ProductPreViewDTO>> getReviewProduct(){
        List<Product> productList = productRepository.findAllByOrderByReviewCountDesc();

        List<ProductResponseDTO.ProductPreViewDTO> productPreViewDTOList = productList.stream()
                .map(ProductConverter::convertToResponseDTO)
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(productPreViewDTOList);
    }
}
