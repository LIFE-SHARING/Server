package com.umc.lifesharing.product.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.heart.repository.HeartRepository;
import com.umc.lifesharing.product.converter.ProductConverter;
import com.umc.lifesharing.product.dto.ProductRequestDTO;
import com.umc.lifesharing.product.dto.ProductResponseDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductCategory;
import com.umc.lifesharing.product.repository.ProductCategoryRepository;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import com.umc.lifesharing.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.List;

@Service
@Repository
@RequiredArgsConstructor
@Transactional
public class ProductCommandServiceImpl implements ProductCommandService{

    private final UserQueryService userQueryService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductQueryService productQueryService;
    private final HeartRepository heartRepository;
    private final ProductCategoryRepository productCategoryRepository;

    // 제품 등록
    @Override
    @Transactional
    public Product ProductRegister(ProductRequestDTO.RegisterProductDTO request, User loggedInMember) {

        Product newProduct = ProductConverter.toProduct(request);

        ProductCategory category = productQueryService.findByCategory(request.getCategoryId()).get();

        newProduct.setUser(loggedInMember);
        newProduct.setCategory(category);

        return productRepository.save(newProduct);
    }

    // 제품 삭제
    @Override
    public void deleteProduct(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.PRODUCT_NOT_FOUND.getMessage()));

        // userAdapter에서 사용자 정보를 가져옴
        //User loggedInUser = userAdapter.getUser();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND.getMessage()));

        // 여기에서 현재 로그인한 회원이 해당 제품의 소유자인지 확인하는 로직 추가
        if (product.getUser().getId().equals(user.getId())) {
            productRepository.delete(product);
        }
        else {
            throw new NotFoundException("로그인해주세요.");
        }

    }

    // 제품 상세 조회
    @Override
    public ProductResponseDTO.ProductDetailDTO productDetail(Long productId, UserAdapter userAdapter) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.PRODUCT_NOT_FOUND.getMessage()));

        // 좋아요 여부 확인 -> 좋아요가 눌러져있다면 해당 회원이 해당 제품을 상세 조회할 때 좋아요 여부가 true
        boolean isLiked = isProductLikedByUser(product, userAdapter);

        ProductResponseDTO.ProductDetailDTO productDetailDTO = ProductConverter.toDetailRes(product);
        productDetailDTO.setIsLiked(isLiked);

        return productDetailDTO;
    }

    // 제품에 대한 좋아요 판별(회원 기준)
    @Override
    public boolean isProductLikedByUser(Product product, UserAdapter userAdapter) {
        // userAdapter에서 사용자 정보를 가져옴
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
//    public void updateProduct(Long productId, ProductRequest.UpdateProductDTO request) {
//        // productId를 사용하여 데이터베이스에서 제품을 가져온다.
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
//
//        // request에서 받은 정보로 제품 정보를 업데이트한다.
//        product.setMemberId(request.getMemberId());
//        product.setName(request.getName());
//        product.setContent(request.getContent());
//        product.setDayPrice(request.getDayPrice());
//        product.setHourPrice(request.getHourPrice());
//        product.setDeposit(request.getDeposit());
//        product.setLendingPeriod(request.getLendingPeriod());
//        product.setFiles(request.getFiles());
//
//        // 업데이트된 제품을 저장한다.
//        productRepository.save(product);
//    }

}
