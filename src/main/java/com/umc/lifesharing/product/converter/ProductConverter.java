package com.umc.lifesharing.product.converter;

import com.umc.lifesharing.product.dto.ProductRequestDTO;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductConverter {

    public static Product toProduct(ProductRequestDTO.RegisterProductDTO request){

        return Product.builder()
                .name(request.getName())
                .content(request.getContent())
                .dayPrice(request.getDayPrice())
                .hourPrice(request.getHourPrice())
                .deposit(request.getDeposit())
                .lendingPeriod(request.getLendingPeriod())
//                .image_url(new ArrayList<>())  // 1/10일 - 이미지도 리스트 형태로 받아와야함(구현전)
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
                        review.getImageUrl(),
                        review.getScore(),
                        review.getContent()
                ))
                .collect(Collectors.toList());

        return ProductResponseDTO.ProductDetailDTO.builder()
                .productId(product.getId())
                .userId(product.getUser().getId())
                .name(product.getName())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .imageUrl(new ArrayList<>())
                .score(product.getScore())
                .reviewCount(product.getReviewCount())
                .deposit(product.getDeposit())
                .dayPrice(product.getDayPrice())
                .hourPrice(product.getHourPrice())
                .content(product.getContent())
                .reviewList(reviewList)
//                찜여부
                .location("사용자로부터 받아오기")
//                .mapInfo(generateMapInfo(product.getUser())
                .build();
    }

    // Product 엔티티를 ProductResponseDTO로 변환하는 로직을 작성
    public static ProductResponseDTO.ProductPreViewDTO convertToResponseDTO(Product product) {
        return ProductResponseDTO.ProductPreViewDTO.builder()
                .productId(product.getId())
                .name(product.getName())
                .score(product.getScore())
                .location("사용자로부터 받아오기")
                .reviewCount(product.getReviewCount())
                .dayPrice(product.getDayPrice())
                .deposit(product.getDeposit())
                //product.get_location
                // 이미지
                .build();
    }

    // 제품 검색 시 응답
    public static ProductResponseDTO.SearchListDTO searchResultDTO(Product product){
        return ProductResponseDTO.SearchListDTO.builder()
                .product_id(product.getId())
                .name(product.getName())
                .score(product.getScore())
                .review_count(product.getReviewCount())
                .day_price(product.getDayPrice())
                .hour_price(product.getHourPrice())
                .deposit(product.getDeposit())
                // 위치정보
                .build();
    }

}
