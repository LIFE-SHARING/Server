package com.umc.lifesharing.product.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.heart.repository.HeartRepository;
import com.umc.lifesharing.product.converter.ProductConverter;
import com.umc.lifesharing.product.dto.ProductRequestDTO;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductImage;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.product.service.ProductCommandService;
import com.umc.lifesharing.product.service.ProductQueryService;
import com.umc.lifesharing.s3.AwsS3Service;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductRestController {

    private final ProductCommandService productCommandService;
    private final AwsS3Service awsS3Service;

    // 제품 등록 API
    @PostMapping(value = "/register", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "제품 등록을 위한 API", description = "제품을 등록할 수 있는 API입니다. 회원과 카테고리를 적어주세요.")
    public ApiResponse<ProductResponseDTO.RegisterResultDTO> registerProduct(@RequestPart @Valid ProductRequestDTO.RegisterProductDTO request,
            @AuthenticationPrincipal UserAdapter userAdapter, @RequestPart(name = "files") List<MultipartFile> files){
        // 파일 업로드 처리
        List<String> uploadedFileNames = awsS3Service.uploadProductFiles(files);
        // 현재 로그인한 사용자의 아이디를 사용하여 제품을 등록
        Product product = productCommandService.ProductRegister(request, userAdapter, uploadedFileNames);

        // 새로운 이미지 리스트 추가
//        for (String imageUrl : uploadedFileNames) {
//
//            ProductImage newProductImage = ProductImage.create(product, imageUrl, url + imageUrl);
//            product.getImages().add(newProductImage);
//        }
        return ApiResponse.onSuccess(ProductConverter.toRegisterResultDTO(product));
    }

    // 제품 삭제 API
    @DeleteMapping("/delete/{productId}")
    @Operation(summary = "제품 삭제 API", description = "해당 제품을 삭제하는 API입니다.")
    @Parameters({@Parameter(name = "productId", description = "path variable에 제품 번호를 넣어주세요.")})
    public ApiResponse<ProductResponseDTO.DeleteRes> deleteProduct(@Positive @PathVariable(name = "productId") Long productId, @AuthenticationPrincipal UserAdapter userAdapter){
        Long loggedInUser = userAdapter.getUser().getId();
        Product deleteProduct = productCommandService.deleteProduct(productId, loggedInUser);
        return ApiResponse.onSuccess(ProductConverter.deleteResult(deleteProduct));
    }

    // 제품 정보 수정 API
    @PatchMapping("/update/{productId}")
    @Operation(summary = "제품 정보 수정 API")
    @Parameter(name = "productId", description = "제품 번호를 넣어주세요.")
    public ApiResponse<ProductResponseDTO.UpdateResDTO> toUpdateProduct(@PathVariable(name = "productId") Long productId, @RequestBody(required = false) ProductRequestDTO.UpdateProductDTO request,
                                                                        @AuthenticationPrincipal UserAdapter userAdapter){
        Product updateProduct = productCommandService.updateProduct(productId, request, userAdapter);
        return ApiResponse.onSuccess(ProductConverter.updateResDTO(updateProduct));
    }

    // 제품 이미지 수정 API
    @PutMapping("/update-image/{productId}")
    @Operation(summary = "제품 이미지 수정 API")
    @Parameter(name = "productId", description = "제품 번호를 넣어주세요.")
    public ApiResponse<ProductResponseDTO.UpdateResDTO> toUpdateImage(@PathVariable(name = "productId") Long productId, @RequestPart(required = false) List<MultipartFile> imageList,
                                             @AuthenticationPrincipal UserAdapter userAdapter){

        // 현재 로그인한 사용자의 아이디를 사용하여 제품 이미지 수정
        Product changeProdut = productCommandService.updateProductImage(productId, userAdapter, imageList);

        return ApiResponse.onSuccess(ProductConverter.updateResDTO(changeProdut));
    }

    // 카테고리별 제품 조회 API
    @GetMapping("/category")
    @Operation(summary = "특정 카테고리의 제품 조회 API", description = "해당 카테고리의 제품을 조회하는 API입니다.")
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리 번호를 넣어주세요.")
    })
    public ApiResponse<ProductResponseDTO.HomePreviewListDTO> getProductByCategory(@ExistCategories @RequestParam(name = "categoryId") Long categoryId){
        List<Product> productList = productCommandService.getProductsByCategory(categoryId);
        return ApiResponse.onSuccess(ProductConverter.homeAndCateList(productList));
    }

    // 제품 상세 조회 API
    @GetMapping("/detail")
    @Operation(summary = "제품 상세 조회 API", description = "제품 상세 조회 API입니다. PathVariable에 제품id를 입력하세요.")
    public ApiResponse<ProductResponseDTO.ProductDetailDTO> getProductDetail(@RequestParam(name = "productId") Long productId, @AuthenticationPrincipal UserAdapter userAdapter){
        ProductResponseDTO.ProductDetailDTO productDetail = productCommandService.productDetail(productId, userAdapter);
        return ApiResponse.onSuccess(productDetail);
    }

    // 홈 필터별 제품 조회 API
    @GetMapping("/home")
    @Operation(summary = "홈에서 필터별 제품 조회")
    @Parameter(name = "filter", description = "필터를 적용하세요. recent / popular / review")
    public ApiResponse<ProductResponseDTO.HomePreviewListDTO> getHomeProduct(@RequestParam(name = "filter", defaultValue = "recent") String filter){
        List<Product> homeProductList = productCommandService.getHomeProduct(filter);
        return ApiResponse.onSuccess(ProductConverter.homeAndCateList(homeProductList));
    }

    // 제품 검색 API
    @GetMapping("/search")
    @Operation(summary = "제품 검색 조회 API")
    public ApiResponse<List<ProductResponseDTO.SearchListDTO>> getSearchProduct(@RequestParam(name = "keyword") String keyword, @RequestParam(name = "filter", defaultValue = "recent") String filter,
                                                                                @AuthenticationPrincipal UserAdapter userAdapter){
        List<Product> searchProductList = productCommandService.getSearchProduct(filter, keyword);

        if(searchProductList.isEmpty()){
            return ApiResponse.onFailure(ErrorStatus.PRODUCT_NOT_FOUND.getCode(), ErrorStatus.PRODUCT_NOT_FOUND.getMessage(), Collections.emptyList());
        }
        else{
            List<ProductResponseDTO.SearchListDTO> searchList = searchProductList.stream()
                    .map(product -> {
                        ProductResponseDTO.SearchListDTO searchListDTO = ProductConverter.searchResultDTO(product);
                        searchListDTO.setIsLiked(productCommandService.isProductLikedByUser(product, userAdapter));
                        return searchListDTO;
                    })
                    .collect(Collectors.toList());
            return ApiResponse.onSuccess(searchList);
        }
    }


    // 마이페이지 - 제품 등록 내역
    @GetMapping("/my-regist")
    @Operation(summary = "마이페이지 제품 등록 내역")
    public ApiResponse<ProductResponseDTO.myRegProductDTO> getMyRegProduct(@AuthenticationPrincipal UserAdapter userAdapter) {
        List<ProductResponseDTO.myRegProductList> myRegProductLists = productCommandService.getMyPageProduct(userAdapter);
        return ApiResponse.onSuccess(ProductConverter.toMyProductReg(myRegProductLists));
    }
  
    @GetMapping("/my")
    @Operation(summary = "제품 검색 조회 API")
    public ApiResponse<List<ProductResponseDTO.MyListDTO>> getMyProduct(@AuthenticationPrincipal UserAdapter userAdapter){
        List<ProductResponseDTO.MyListDTO> myProductList = productCommandService.getMyProduct(userAdapter.getUser());

        return ApiResponse.onSuccess(myProductList);

    }

    // 제품 수정 페이지 진입 시 정보 요청 API
    @GetMapping("/info/{productId}")
    @Operation(summary = "기존 제품 정보 전달 API")
    public ApiResponse<ProductResponseDTO.ForProductUpdateDTO> getProductInfo(@PathVariable(name = "productId") Long productId, @AuthenticationPrincipal UserAdapter userAdapter){
        Product product = productCommandService.getProductInfo(productId, userAdapter);
        return ApiResponse.onSuccess(ProductConverter.forProductUpdate(product));
    }
}
