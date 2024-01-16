package com.umc.lifesharing.product.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
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
import org.springframework.context.annotation.Bean;
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

    // 제품 등록
    @Override
    public Product ProductRegister(ProductRequestDTO.RegisterProductDTO request) {
        Product newProduct = ProductConverter.toProduct(request);

        User user = userQueryService.findByUser(request.getMemberId()).get();
        ProductCategory category = productQueryService.findByCategory(request.getCategoryId()).get();

        newProduct.setUser(user);  // 제품을 등록한 회원id 설정
        newProduct.setCategory(category);  // 등록한 제품에 카테고리 설정

        return productRepository.save(newProduct);
    }

    // 제품 삭제
    @Override
    public void deleteProduct(Long productId, Long userId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.PRODUCT_NOT_FOUND.getMessage()));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND.getMessage()));

        // 여기에서 현재 로그인한 회원이 해당 제품의 소유자인지 확인하는 로직 추가
//        if (!product.getUser().getUsername().equals(username)) {
//            throw new UnauthorizedException("You do not have permission to delete this product.");
//        }

        productRepository.delete(product);
    }

    // 제품 상세 조회
    @Override
    public ProductResponseDTO.ProductDetailDTO productDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorStatus.PRODUCT_NOT_FOUND.getMessage()));

        return ProductConverter.toDetailRes(product);
    }

    // 카테고리별 제품 조회
    @Override
    public List<Product> getProductsByCategory(Long categoryId) {
        List<Product> productList = productRepository.findByCategoryId(categoryId);

        return productList;
    }

    @Override
    public void updateProductScore(Long productId, Integer newScore) {
        productRepository.updateScore(productId, newScore);
    }

}
