package com.umc.lifesharing.product.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.handler.ProductHandler;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.heart.repository.HeartRepository;
import com.umc.lifesharing.product.converter.ProductConverter;
import com.umc.lifesharing.product.dto.ProductRequestDTO;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductCategory;
import com.umc.lifesharing.product.entity.ProductImage;
import com.umc.lifesharing.product.repository.ProductCategoryRepository;
import com.umc.lifesharing.product.repository.ProductImageRepository;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.review.entity.Review;
import com.umc.lifesharing.review.rerpository.ReviewRepository;
import com.umc.lifesharing.reservation.entity.Reservation;
import com.umc.lifesharing.reservation.entity.enum_class.Status;
import com.umc.lifesharing.reservation.repository.ReservationRepository;
import com.umc.lifesharing.s3.AwsS3Service;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import com.umc.lifesharing.user.service.UserQueryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Repository
@RequiredArgsConstructor
@Transactional
public class ProductCommandServiceImpl implements ProductCommandService{

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final UserRepository userRepository;
    private final HeartRepository heartRepository;
    private final AwsS3Service awsS3Service;
    private final ProductCategoryRepository productCategoryRepository;
    private final ReservationRepository reservationRepository;

    @Value("${s3.url}")
    private String url;

    // 제품 등록
    @Override
    @Transactional
    public Product ProductRegister(ProductRequestDTO.RegisterProductDTO request, UserAdapter userAdapter, List<String> uploadedFileNames) {

        Product newProduct = ProductConverter.toProduct(request);

        User loggedInMember = userAdapter.getUser();

        ProductCategory category = productCategoryRepository.findById(request.getCategoryId()).get();

        newProduct.setUser(loggedInMember);
        newProduct.setCategory(category);

        // 이미지 URL을 ProductImage 엔티티로 매핑하여 리스트에 추가
        for (String imageUrl : uploadedFileNames) {
            String fullUrl = url + imageUrl;
            ProductImage productImage = ProductImage.create(imageUrl);
            productImage.setFullImageUrl(fullUrl);
            productImage.setProduct(newProduct);
            newProduct.getImages().add(productImage);
        }

        return productRepository.save(newProduct);
    }

    // 제품 이미지 수정
    @Override
    @Transactional
    public void updateProductImage(Long productId, UserAdapter userAdapter, List<MultipartFile> imageList) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));
        User user = userRepository.findById(userAdapter.getUser().getId()).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));

        // 기존의 이미지 리스트를 삭제
        product.getImages().forEach(productImage -> {
            awsS3Service.deleteProductFile(productImage.getImageUrl());
            productImageRepository.delete(productImage);
        });
        product.getImages().clear();

        // 파일 업로드 처리
        List<String> uploadedFileNames = awsS3Service.uploadProductFiles(imageList);

        // 새로운 이미지 리스트 추가
        for (String imageUrl : uploadedFileNames) {
            ProductImage newProductImage = ProductImage.create(product, imageUrl, url + imageUrl);
            product.getImages().add(newProductImage);
        }
    }

    @Override
    // 제품 정보 수정
    public Product updateProduct(Long productId, ProductRequestDTO.UpdateProductDTO request, UserAdapter userAdapter) {
        // productId를 사용하여 데이터베이스에서 제품을 가져온다.
        Product existProduct = productRepository.findById(productId).orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        User loggedInUser = userAdapter.getUser();

        if(!existProduct.getUser().getId().equals(loggedInUser.getId())){   // 등록자와 수정하고자 하는 사용자가 일치하지 않으면
            throw new UserHandler(ErrorStatus.USER_NOT_FOUNDED);
        }
        if (request.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ProductHandler(ErrorStatus.CATEGORY_NOT_FOUND));
            // 제품에 새로운 카테고리 설정
            existProduct.setCategory(category);
        }
        if (request.getName() != null){ existProduct.setName(request.getName()); }
        if (request.getContent() != null){ existProduct.setContent(request.getContent()); }
        if (request.getDayPrice() != null){ existProduct.setDayPrice(request.getDayPrice()); }
        if (request.getHourPrice() != null){ existProduct.setHourPrice(request.getHourPrice()); }
        if (request.getDeposit() != null){ existProduct.setDeposit(request.getDeposit()); }
        if (request.getLendingPeriod() != null){ existProduct.setLendingPeriod(request.getLendingPeriod()); }

        return productRepository.save(existProduct);
    }

    // 제품 삭제
    @Override
    public void deleteProduct(Long productId, Long userId) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 여기에서 현재 로그인한 회원이 해당 제품의 소유자인지 확인하는 로직 추가
        if (product.getUser().getId().equals(user.getId())) {

            // 제품에 연결된 이미지를 먼저 삭제
            List<ProductImage> productImages = product.getImages();
            for (ProductImage productImage : productImages) {
                // S3에서 이미지 파일 삭제
                awsS3Service.deleteProductFile(productImage.getImageUrl());
                //awsS3Service.deleteFileByUrl(productImage.getImageUrl());

                // 이미지 엔티티 삭제
                productImageRepository.delete(productImage);
            }
            // 제품 삭제
            productRepository.delete(product);
        }
        else {
            throw new NotFoundException("로그인해주세요.");
        }
    }

    // 제품 상세 조회
    @Override
    public ProductResponseDTO.ProductDetailDTO productDetail(Long productId, UserAdapter userAdapter) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException(ErrorStatus.PRODUCT_NOT_FOUND.getMessage()));

        // 좋아요 여부 확인 -> 좋아요가 눌러져있다면 해당 회원이 해당 제품을 상세 조회할 때 좋아요 여부가 true
        boolean isLiked = isProductLikedByUser(product, userAdapter);

        // join fetch로 어떤 Product의 User 를 찾도록 함
        Optional<Product> optionalProduct = productRepository.findProductWithUser(productId);
        Product p = optionalProduct.get();
        User u = p.getUser();

        ProductResponseDTO.ProductDetailDTO productDetailDTO = ProductConverter.toDetailRes(product);
        productDetailDTO.setIsLiked(isLiked);
        productDetailDTO.setUserNickname(u.getName());
        productDetailDTO.setUserImage(u.getProfileUrl());

        return productDetailDTO;
    }

    // 제품에 대한 좋아요 판별(회원 기준)
    @Override
    public boolean isProductLikedByUser(Product product, UserAdapter userAdapter) {
        User loggedInUser = userAdapter.getUser();

        // 사용자가 현재 제품에 대해 좋아요를 눌렀는지 확인
        return heartRepository.findByUserAndProduct(loggedInUser, product).isPresent();
    }

    // 카테고리별 제품 조회
    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        List<Product> productList = productRepository.findByCategoryIdOrderByCreatedAtDesc(categoryId);
        return productList;
    }

    // 제품에 대한 평점 업데이트
    @Override
    public void updateProductScore(Long productId, Integer newScore) {
        productRepository.updateScore(productId, newScore);
    }

    // 홈에서 필터별 제품 조회
    @Override
    public List<Product> getHomeProduct(String filter) {

        List<Product> productList = null;

        if (filter.equals("recent")){
            productList = productRepository.findAllByOrderByCreatedAtDesc();
        }
        else if (filter.equals("popular")){
            productList = productRepository.findAllByOrderByScoreDesc();
        }
        else if (filter.equals("review")){
            productList = productRepository.findAllByOrderByReviewCountDesc();
        }
        return productList;
    }

    // 제품 검색 시 필터별 조회
    @Override
    public List<Product> getSearchProduct(String filter, String keyword) {
        List<Product> productList = null;

        if (!isValidKeyword(keyword)) {   // 검색어가 유효하지 않다면 빈 리스트 리턴
            return Collections.emptyList();
        }

        if (filter.equals("recent")){
            productList = productRepository.findByNameContainingOrderByCreatedAtDesc(keyword);
        }
        else if (filter.equals("popular")){
            productList = productRepository.findByNameContainingOrderByScoreDesc(keyword);
        }
        else if (filter.equals("review")){
            productList = productRepository.findByNameContainingOrderByReviewCountDesc(keyword);
        }
        return productList;
    }

    // 마이페이지 제품 등록 내역
    @Override
    public List<ProductResponseDTO.myRegProductList> getMyPageProduct(UserAdapter userAdapter) {
        User user = userRepository.findById(userAdapter.getUser().getId()).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUNDED));

        List<Product> productList = productRepository.findAllByUser(user);

        List<ProductResponseDTO.myRegProductList> registList = new ArrayList<>();

        for (Product product : productList){
            String lendingDay = product.getLendingPeriod();

            List<String> dateList = parseDate(lendingDay);

            // 시작일과 종료일을 추출
            String startDate = dateList.size() > 0 ? dateList.get(0) : null;
            String endDate = dateList.size() > 1 ? dateList.get(dateList.size() - 1) : null;

            ProductResponseDTO.myRegProductList myRegProductList = ProductConverter.toMyRegProduct(product);
            myRegProductList.setStartDate(startDate);
            myRegProductList.setEndDate(endDate);

            // 리스트에 추가
            registList.add(myRegProductList);
        }
        return registList;
    }

    private List<String> parseDate(String dateString) {
        // 대여 기간 문자열에서 "M.d(EEE)" 패턴을 찾기
        Pattern pattern = Pattern.compile("(\\d{1,2}\\.\\d{1,2}\\([^)]+\\))");
        Matcher matcher = pattern.matcher(dateString);

        List<String> dateList = new ArrayList<>();
        
        while (matcher.find()) {
            dateList.add(matcher.group(1));
        }
        return dateList;
    }

    // 키워드 검사
    private boolean isValidKeyword(String keyword){
        return keyword.length() >= 2;
    }


    // 제품 검색 시 필터별 조회
    @Override
    public List<ProductResponseDTO.MyListDTO> getMyProduct(User user) {
        // 반환할 리스트
        List<ProductResponseDTO.MyListDTO> myListDTO = new ArrayList<>();

        List<Product> productList = productRepository.findAllByUser(user);
        List<Reservation> reservationList = reservationRepository.findAllByProductInAndStatus(productList, Status.ACTIVE);

        //제품 추가
        myListDTO = productList.stream()
                .map(product -> {
                    ProductResponseDTO.MyListDTO toMyDto = ProductConverter.toMyResultDTO(product, null);
                    return toMyDto;
                })
                .collect(Collectors.toList());

        // 현재 날짜 구하기
        LocalDateTime now = LocalDateTime.now();


        // 포맷 정의
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        for(Reservation r : reservationList){
            if(r.getEndDate().isAfter(now) && r.getStartDate().isBefore(now)){
                // 포맷 적용
                String start = r.getStartDate().format(formatter);
                String end = r.getEndDate().format(formatter);
                String lentDate = start+"-"+end+"\n대여중";

                // 이미 제품이 리스트에 추가되었는지 확인
                for(ProductResponseDTO.MyListDTO p : myListDTO){

                    if(p.getProduct_id().equals(r.getProduct().getId())){
                        p.setIsReserved(lentDate); // 대여 시작일-종료일과 대여중임을 표시
                    }
                }

            }
        }

        return myListDTO.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public Integer otherAverageScoreByUserId(Long userId) {
        List<Product> userProducts = productRepository.findAllByUserId(userId);

        if (userProducts.isEmpty()) {
            return 0; // 사용자가 등록한 제품이 없으면 0.0 반환 또는 다른 적절한 값을 반환
        }

        // 제품들의 평균 별점 계산
        Integer totalScore = userProducts.stream()
                .mapToInt(Product::getScore)
                .sum();

        return totalScore / userProducts.size();
    }
}
