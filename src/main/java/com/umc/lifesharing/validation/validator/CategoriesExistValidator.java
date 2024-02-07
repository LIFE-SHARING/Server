package com.umc.lifesharing.validation.validator;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.entity.ProductCategory;
import com.umc.lifesharing.product.repository.ProductCategoryRepository;
import com.umc.lifesharing.product.service.ProductQueryService;
import com.umc.lifesharing.validation.annotation.ExistCategories;
import com.umc.lifesharing.validation.annotation.ExistMembers;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriesExistValidator implements ConstraintValidator<ExistCategories, Long> {

    private final ProductCategoryRepository productCategoryRepository;


    @Override
    public void initialize(ExistCategories constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<ProductCategory> target = productCategoryRepository.findById(value);
        if (target.isEmpty()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.CATEGORY_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
