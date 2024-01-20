package com.umc.lifesharing.product.converter;

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

    // 카테고리별 제품 조회
//    public static ProductResponse.ProductPreViewDTO productPreviewDTO(Product product){
//        return ProductResponse.ProductPreViewDTO.builder()
//                .product_id(product.getId())
//                .name(product.getName())
//                .deposit(product.getDeposit())
//                .day_price(product.getDay_price())
//                .score(product.getScore()) //별점(평균으로 가져오도록 해야함 - 구현전)
//                .review_count(product.getScore_count()) //리뷰 개수(해당 제품에 대한 리뷰 개수를 카운트해야함 - 구현전)
//                //위치
//                .build();
//    }

//    public static List<ProductPrefer> toProductPreferList(List<ProductCategory> productCategoryList){
//
//        return productCategoryList.stream()
//                .map(productCategory ->
//                        ProductPrefer.builder()
//                                .category(productCategory)
//                                .build()
//                ).collect(Collectors.toList());
//    }
//
//    public static ProductResponseDTO.ProductPreViewDTO convertToResponseDTO(Product product) {
//        // Product 엔티티를 ProductResponseDTO로 변환하는 로직을 작성
//        // 예시로 빈 생성자를 사용하는 가정한 코드
//        return new ProductResponseDTO.ProductPreViewDTO(
//                product.getId(),
//                product.getName(),
//                product.getDayPrice(),
//                product.getDeposit(),
//                product.getScore(),
//                product.getReviewCount()
//                // 위치 product.get_location
//                // 나머지 필드들에 대한 매핑
//        );
//    }

}
