package com.umc.lifesharing.product.converter;

import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductCategory;

import java.util.List;
import java.util.stream.Collectors;

public class ProductPreferConverter {
//    public static List<ProductPrefer> toProductPreferList(List<ProductCategory> foodCategoryList){
//
//        return foodCategoryList.stream()
//                .map(foodCategory ->
//                        ProductPrefer.builder()
//                                .category(foodCategory)
//                                .build()
//                ).collect(Collectors.toList());
//    }

     //카테고리별 제품 조회
    public static ProductResponseDTO.ProductPreViewDTO productPreviewDTO(Product product){
        return ProductResponseDTO.ProductPreViewDTO.builder()
                .productId(product.getId())
                .name(product.getName())
                .deposit(product.getDeposit())
                .dayPrice(product.getDayPrice())
                .score(product.getScore()) //별점(평균으로 가져오도록 해야함 - 구현전)
                .reviewCount(product.getReviewCount()) //리뷰 개수(해당 제품에 대한 리뷰 개수를 카운트해야함 - 구현전)
                .location(product.getUser().getLocationList().get(0).getDong())
                .image_url(product.getImages().get(0).getImageUrl())//위치
                .build();
    }

    public static List<SelectCategory> toProductPreferList(List<ProductCategory> productCategoryList){

        return productCategoryList.stream()
                .map(productCategory ->
                        SelectCategory.builder()
                                .category(productCategory)
                                .build()
                ).collect(Collectors.toList());
    }

    public static ProductResponseDTO.ProductPreViewDTO convertToResponseDTO(Product product) {
        // Product 엔티티를 ProductResponseDTO로 변환하는 로직을 작성
        // 예시로 빈 생성자를 사용하는 가정한 코드
        return new ProductResponseDTO.ProductPreViewDTO(
                product.getId(),
                product.getName(),
                product.getUser().getLocationList().get(0).getDong(),// 위치
                product.getDayPrice(),
                product.getDeposit(),
                product.getScore(),
                product.getReviewCount(),
                product.getImages().get(0).getImageUrl()// 나머지 필드들에 대한 매핑
        );
    }

}
