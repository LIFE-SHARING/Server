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
                        review.getLent_day(),
                        review.getImage_url(),
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
                .content(product.getContent())
                .reviewList(reviewList)
//                찜여부
//                .location(getLocationInfo(product.getUser()))
//                .mapInfo(generateMapInfo(product.getUser())
                .build();
    }

    // Product 엔티티를 ProductResponseDTO로 변환하는 로직을 작성
    public static ProductResponseDTO.ProductPreViewDTO convertToResponseDTO(Product product) {
        return new ProductResponseDTO.ProductPreViewDTO(
                product.getId(),
                product.getName(),
                product.getDayPrice(),
                product.getDeposit(),
                product.getScore(),
                product.getReviewCount()
                // 위치 product.get_location
                // 나머지 필드들에 대한 매핑
        );
    }

//    public static ProductResponseDTO.MapInfoDTO generateMapInfo(Location locationInfo) {
//        // 여기에서 위치 정보를 이용하여 지도 정보를 생성하는 로직을 구현
//        // 생성된 지도 정보를 MapInfoDTO 객체에 담아 반환
//        // 예시로 더미 데이터를 사용한 경우:
//        return ProductResponseDTO.MapInfoDTO.builder()
//                .mapImageUrl("https://example.com/map_image")
//                // ... (다른 지도 정보 필드들)
//                .build();
//    }

//    public Location getLocationInfo(User user) {
//        return user.getLocation();
//    }
}
