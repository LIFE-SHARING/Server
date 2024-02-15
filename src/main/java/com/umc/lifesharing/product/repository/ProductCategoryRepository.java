package com.umc.lifesharing.product.repository;

import com.umc.lifesharing.product.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    List<String> findNameByIdIn(List<Long> ids);

    List<ProductCategory> findAllByIdIn(List<Long> ids);

    Optional<String> findNameById(Long id);
}
