package com.umc.lifesharing.product.converter;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.ProductHandler;
import com.umc.lifesharing.product.dto.ProductRequestDTO;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductImage;
import com.umc.lifesharing.reservation.entity.Reservation;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.entity.ReviewImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
                        review.getUser().getName(),
                        review.getUser().getProfileUrl(),   // 리뷰 작성자 프로필 이미지
                        review.getCreatedAt(),
                        getLentDayFromReservation(review.getReservation()),
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
//                .categoryId(product.getCategory().getId())
                .categoryName(new ArrayList<>())
                .imageUrl(imageUrls)  // 등록된 제품 이미지 리스트
                .score(product.getScore())
                .reviewCount(product.getReviewCount())
                .deposit(product.getDeposit())
                .dayPrice(product.getDayPrice())
                .hourPrice(product.getHourPrice())
                .content(product.getContent())
                .reviewList(reviewList)
                .location(product.getUser().getLocationList().get(0).getDong())    // 읍/면/동    ex. 상암동
                .detailAddress(product.getUser().getLocationList().get(0).getRoadAddress())  // 도로명 ex. 주소 서울특별시 마포구 성암로 301
                .build();
    }

    // getLentDay 메서드 정의
    public static String getLentDayFromReservation(Reservation reservation) {
        // 예약이 널이 아닌지 확인
        if (reservation != null) {
            // 예약의 totalTime 필드 반환
            return reservation.getTotalTime();
        }
        return null; // 또는 다른 기본값 또는 예외 처리 방법 선택
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
                .location(product.getUser().getLocationList().get(0).getDong())
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
                .location(product.getUser().getLocationList().get(0).getDong())
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
    // 전체 이미지 URL 가져오기
//    private static List<String> getProductImagUrls(Product product/*, String baseUrl*/) {
//        return product.getImages().stream()
//                .map(image -> {
//                    String imageUrl = image.getImageUrl(); // 이미지의 상대 경로
//
//                    // 이미지 URL이 null이 아니고, 상대 경로인 경우에만 baseUrl을 추가
//                    return imageUrl != null && !imageUrl.startsWith("https") ? "https://lifesharing.s3.ap-northeast-2.amazonaws.com/" + imageUrl : imageUrl;
//                })
//                .collect(Collectors.toList());
//    }


    // 제품 정보 수정 응답
    public static ProductResponseDTO.UpdateResDTO updateResDTO(Product product){
        return ProductResponseDTO.UpdateResDTO.builder()
                .productId(product.getId())
                .updatedAt(LocalDateTime.now())
                .build();
    }
  

    // 마이페이지 등록 내역 응답
    public static ProductResponseDTO.myRegProductList toMyRegProduct(Product product/*, String start, String end*/){
        List<String> imagUrls= ProductConverter.getProductImagUrls(product);

        String firstImageUrl = imagUrls.isEmpty() ? null : imagUrls.get(0);

        return ProductResponseDTO.myRegProductList.builder()
                .productId(product.getId())
                .name(product.getName())
                .location(product.getUser().getLocationList().get(0).getDong())   // 27일 - 사용자 위치 받아오기
                .imageUrl(firstImageUrl)
                .build();
    }

    public static ProductResponseDTO.myRegProductDTO toMyProductReg(List<ProductResponseDTO.myRegProductList> productList){
        return ProductResponseDTO.myRegProductDTO.builder()
                .productCount(productList.size())
                .myRegProductList(productList)
                .build();
    }

    // 내가 등록한 제품 조회시 응답
    public static ProductResponseDTO.MyListDTO toMyResultDTO(Product product, String lent){

        List<String> imageUrls = ProductConverter.getProductImagUrls(product);
        // 이미지 리스트에서 첫 번째 이미지 가져오기
        String firstImageUrl = imageUrls.isEmpty() ? null : imageUrls.get(0);

        return ProductResponseDTO.MyListDTO.builder()
                .product_id(product.getId())
                .name(product.getName())
                .image_url(firstImageUrl)
                .score(product.getScore())
                .review_count(product.getReviewCount())
                .day_price(product.getDayPrice())
                .hour_price(product.getHourPrice())
                .deposit(product.getDeposit())
                .isReserved(lent)
                .location(product.getUser().getLocationList().get(0).getDong())
                .build();
    }

//    public static List<ProductResponseDTO.MyListDTO> toMyResultList(List<RestaurantCategory> restaurantCategoryList){
//
//        return List<ProductResponseDTO.MyListDTO> toMyResultList() = searchProductList.stream()
//                .map(product -> {
//                    ProductResponseDTO.SearchListDTO searchListDTO = ProductConverter.searchResultDTO(product);
//                    searchListDTO.setIsLiked(productCommandService.isProductLikedByUser(product, userAdapter));
//                    return searchListDTO;
//                })
//                .collect(Collectors.toList());
//    }

    // 제품 이미지 수정 응답
    public static ProductResponseDTO.UpdateResDTO updateImageRes(List<MultipartFile> imageLlist){
        return ProductResponseDTO.UpdateResDTO.builder()
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // 제품 삭제 응답
    public static ProductResponseDTO.DeleteRes deleteResult(Product product){
        return ProductResponseDTO.DeleteRes.builder()
                .productId(product.getId())
                .deletedAt(LocalDateTime.now())
                .build();
    }

    // 제품 정보 수정 페이지 진입 응답
    public static ProductResponseDTO.ForProductUpdateDTO forProductUpdate(Product product){
        return ProductResponseDTO.ForProductUpdateDTO.builder()
                //.categoryId(product.getCategory().getId())
                .categoryId(new ArrayList<>())
                .name(product.getName())
                .deposit(product.getDeposit())
                .dayPrice(product.getDayPrice())
                .hourPrice(product.getHourPrice())
                .lendingPeriod(product.getLendingPeriod())
                .content(product.getContent())
                .build();
    }

}
