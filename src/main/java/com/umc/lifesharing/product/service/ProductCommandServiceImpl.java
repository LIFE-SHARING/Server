package com.umc.lifesharing.product.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.product.converter.ProductConverter;
import com.umc.lifesharing.product.dto.ProductRequestDTO;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductCategory;
import com.umc.lifesharing.product.repository.ProductCategoryRepository;
import com.umc.lifesharing.product.repository.ProductRepository;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCommandServiceImpl implements ProductCommandService{

    private final UserQueryService userQueryService;
    private final ProductRepository productRepository;
    private final ProductQueryService productQueryService;

    @Override
    public Product ProductRegister(ProductRequestDTO.RegisterProductDTO request) {
        Product newProduct = ProductConverter.toProduct(request);

        User user = userQueryService.findByUser(request.getMember_id()).get();
        ProductCategory category = productQueryService.findByCategory(request.getCategory_id()).get();

        newProduct.setUser(user);  // 제품을 등록한 회원id 설정
        newProduct.setCategory(category);  // 등록한 제품에 카테고리 설정

        return productRepository.save(newProduct);
    }

}
