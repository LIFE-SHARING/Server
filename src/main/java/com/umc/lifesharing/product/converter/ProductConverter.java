package com.umc.lifesharing.product.converter;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.ProductHandler;
import com.umc.lifesharing.product.dto.ProductRequestDTO;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductImage;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.entity.ReviewImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductConverter {

    @Value("${s3.url}")
    private static String s3BaseUrl;

    public static Product toProduct(ProductRequestDTO.RegisterProductDTO request){
        return Product.builder()
                .name(request.getName())
                .content(request.getContent())
                .dayPrice(request.getDayPrice())
                .hourPrice(request.getHourPrice())
                .deposit(request.getDeposit())
                .lendingPeriod(request.getLendingPeriod())
                .build();
    }
    
    // 제품 등록 응답
    public static ProductResponseDTO.RegisterResultDTO toRegisterResultDTO(Product product){
        return ProductResponseDTO.RegisterResultDTO.builder()
                .productId(product.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 제품 상세 조회 응답
    public static ProductResponseDTO.ProductDetailDTO toDetailRes(Product product){

        List<ProductResponseDTO.ReviewListDTO> reviewList = product.getReviewList().stream()
                .map(review -> new ProductResponseDTO.ReviewListDTO(
                        review.getId(),
                        review.getUser().getId(),
                        review.getCreatedAt(),
                        review.getLentDay(),
                        getReviewImageUrls(review),
                        review.getScore(),
                        review.getContent()
                ))
                .collect(Collectors.toList());

        List<String> imageUrls = ProductConverter.getProductImagUrls(product);

        return ProductResponseDTO.ProductDetailDTO.builder()
                .productId(product.getId())
                .userId(product.getUser().getId())
                .name(product.getName())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .imageUrl(imageUrls)  // 등록된 이미지 리스트
                .score(product.getScore())
                .reviewCount(product.getReviewCount())
                .deposit(product.getDeposit())
                .dayPrice(product.getDayPrice())
                .hourPrice(product.getHourPrice())
                .content(product.getContent())
                .reviewList(reviewList)
                .location("사용자로부터 받아오기")
//                .latitude()
//                .mapInfo(generateMapInfo(product.getUser())
                .build();
    }
    
    // 제품 검색 시 응답
    public static ProductResponseDTO.SearchListDTO searchResultDTO(Product product){

        List<String> imageUrls = ProductConverter.getProductImagUrls(product);
        // 이미지 리스트에서 첫 번째 이미지 가져오기
        String firstImageUrl = imageUrls.isEmpty() ? null : imageUrls.get(0);

        return ProductResponseDTO.SearchListDTO.builder()
                .product_id(product.getId())
                .name(product.getName())
                .image_url(firstImageUrl)
                .score(product.getScore())
                .review_count(product.getReviewCount())
                .day_price(product.getDayPrice())
                .hour_price(product.getHourPrice())
                .deposit(product.getDeposit())
                .location("사용자로부터 받아오기")
                .build();
    }

    // 홈 제품 조회 응답, 카테고리별 제품 조회 응답
    public static ProductResponseDTO.HomeResultDTO getHomeAndCateProduct(Product product){

        List<String> imageUrls = ProductConverter.getProductImagUrls(product);
        // 이미지 리스트에서 첫 번째 이미지 가져오기
        String firstImageUrl = imageUrls.isEmpty() ? null : imageUrls.get(0);

        return ProductResponseDTO.HomeResultDTO.builder()
                .productId(product.getId())
                .name(product.getName())
                .image_url(firstImageUrl)
                .score(product.getScore())
                .reviewCount(product.getReviewCount())
                .deposit(product.getDeposit())
                .dayPrice(product.getDayPrice())
                .location("사용자로부터 받아오기")
                .build();
    }

    public static ProductResponseDTO.HomePreviewListDTO homeAndCateList(List<Product> productList){
        List<ProductResponseDTO.HomeResultDTO> homeResultDTOS = productList.stream()
                .map(ProductConverter::getHomeAndCateProduct)
                .collect(Collectors.toList());

        return ProductResponseDTO.HomePreviewListDTO.builder()
                .productResultDTOList(homeResultDTOS)
                .build();
    }

    // 리뷰 이미지 가져오도록
    private static List<String> getReviewImageUrls(Review review) {
        return review.getImages().stream().map(ReviewImage::getFullImageUrl).collect(Collectors.toList());
    }

    // 제품 이미지 가져오도록
    private static List<String> getProductImagUrls(Product product){
        return product.getImages().stream()
                .map(ProductImage::getFullImageUrl)
                .collect(Collectors.toList());
    }


    // 제품 정보 수정 응답
    public static ProductResponseDTO.UpdateResDTO updateResDTO(Product product){
        return ProductResponseDTO.UpdateResDTO.builder()
                .productId(product.getId())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // 제품 이미지 수정 응답
//    public static ProductResponseDTO.UpdateResDTO updateImageDTO(ProductImage productImage){
//        return ProductResponseDTO.UpdateResDTO.builder()
//                .productId(productImage.getProduct().getId())
//                .updatedAt(LocalDateTime.now())
//                .build();
//    }

    // 마이페이지 등록 내역 응답
    public static ProductResponseDTO.myRegProductList toMyRegProduct(Product product/*, String start, String end*/){
        List<String> imagUrls= ProductConverter.getProductImagUrls(product);

        String firstImageUrl = imagUrls.isEmpty() ? null : imagUrls.get(0);

        return ProductResponseDTO.myRegProductList.builder()
                .name(product.getName())
                .location("사용자로부터 받아오기")   // 27일 - 사용자 위치 받아오기
                .imageUrl(firstImageUrl)
                .build();
    }

    public static ProductResponseDTO.myRegProductDTO toMyProductReg(List<ProductResponseDTO.myRegProductList> productList){
        return ProductResponseDTO.myRegProductDTO.builder()
                .productCount(productList.size())
                .myRegProductList(productList)
                .build();
    }

}
