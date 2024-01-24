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
import com.umc.lifesharing.s3.AwsS3Service;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import com.umc.lifesharing.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
    private final ReviewRepository reviewRepository;

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
            ProductImage productImage = ProductImage.create(imageUrl);
            productImage.setProduct(newProduct);
            newProduct.getImages().add(productImage);
        }
        return productRepository.save(newProduct);
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
                awsS3Service.deleteFile(productImage.getImageUrl());

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
        List<Product> productList = productRepository.findByCategoryId(categoryId);
        return productList;
    }

    // 제품에 대한 평점 업데이트
    @Override
    public void updateProductScore(Long productId, Integer newScore) {
        productRepository.updateScore(productId, newScore);
    }

    // 제품 정보 수정
    public Product updateProduct(Long productId, ProductRequestDTO.UpdateProductDTO request, UserAdapter userAdapter) {
        // productId를 사용하여 데이터베이스에서 제품을 가져온다.
        Product newUpdateProduct = productRepository.findById(productId).orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));

        User loggedInUser = userAdapter.getUser();

        if(!newUpdateProduct.getUser().getId().equals(loggedInUser.getId())){   // 등록자와 수정하고자 하는 사용자가 일치하지 않으면
            throw new UserHandler(ErrorStatus.USER_NOT_FOUNDED);
        }

//        // 새로운 이미지 업로드
//        List<String> newImageUrl = awsS3Service.uploadProductFiles(newImage);
////        List<String> existImageUrl = newUpdateProduct.getImages().stream()
////                .map(ProductImage::getImageUrl)
////                .collect(Collectors.toList());
//
//        // 이미지 추가 : 새로운 이미지 중 기존 이미지 목록에 없는 이미지는 추가
//        List<String> imageToAdd = newImageUrl.stream()
//                .filter(imageUrl -> !newUpdateProduct.getImages().stream()
//                        .map(ProductImage::getImageUrl)
//                        .collect(Collectors.toList())
//                        .contains(imageUrl))
//                .collect(Collectors.toList());
//
//        // 새로운 이미지 추가
//        newUpdateProduct.getImages().addAll(imageToAdd.stream().map(ProductImage::new).collect(Collectors.toList()));
//
//        // 이미지 삭제
//        List<String> imageToDelete = newUpdateProduct.getImages().stream()
//                .map(ProductImage::getImageUrl)
//                .filter(imageUrl -> !newImageUrl.contains(imageUrl))
//                .collect(Collectors.toList());
//
//       // 삭제된 이미지를 식별하고 삭제
//        awsS3Service.deleteProductImages(imageToDelete);
//
//        // 이미지가 업데이트 되면 기존의 이미지 리스트를 새 리스트로 바꾸기
//        newUpdateProduct.getImages().clear();
//        newUpdateProduct.getImages().addAll(newImageUrl.stream().map(ProductImage::new).collect(Collectors.toList()));
//
//

        // request에서 받은 정보로 제품 정보를 업데이트한다.
        newUpdateProduct.setName(request.getName());
        newUpdateProduct.setContent(request.getContent());
        newUpdateProduct.setDayPrice(request.getDayPrice());
        newUpdateProduct.setHourPrice(request.getHourPrice());
        newUpdateProduct.setDeposit(request.getDeposit());
        newUpdateProduct.setLendingPeriod(request.getLendingPeriod());
        //product.setFiles(request.getFiles());

        return productRepository.save(newUpdateProduct);
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

    // 키워드 검사
    private boolean isValidKeyword(String keyword){
        return keyword.length() >= 2;
    }

//    @Override
//    public void addImagesToProduct(Long productId, List<MultipartFile> images) {
//        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductHandler(ErrorStatus.PRODUCT_NOT_FOUND));
//
//        // 기존의 상품 리스트
//        List<String> productImg = product.getImages().stream().map(ProductImage::getImageUrl).collect(Collectors.toList());
//
//        // 새로운 이미지 리스트
//        List<String> newImg = awsS3Service.uploadProductFiles(images);
//
//        // 4. 이미지 추가, 업데이트, 및 삭제 로직 수행
//        List<String> updatedImages = awsS3Service.autoImagesUploadAndDelete(productImg, convertToMultipartFiles(newImg), "product");
//
//        // 5. 상품의 이미지 리스트를 갱신
//        List<ProductImage> updatedProductImages = new ArrayList<>();
//        for (String imageUrl : updatedImages) {
//            ProductImage productImage = new ProductImage();
//            productImage.setImageUrl(imageUrl);
//            productImage.setProduct(product);
//            updatedProductImages.add(productImage);
//        }
//        product.setImages(updatedProductImages);
//
//        // 6. 상품 업데이트
//        productRepository.save(product);
//    }

//    private List<MultipartFile> convertToMultipartFiles(List<String> imageUrls) {
//        return imageUrls.stream()
//                .map(imageUrl -> {
//                    // 이미지 URL로부터 MultipartFile을 생성하는 로직을 추가해야 합니다.
//                    // 예시로 더미 MultipartFile을 생성하도록 하겠습니다.
//                    return new MockMultipartFile("file", imageUrl.getBytes());
//                })
//                .collect(Collectors.toList());
//    }

}
