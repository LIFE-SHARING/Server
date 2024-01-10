package com.umc.lifesharing.validation.validator;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.service.ProductQueryService;
import com.umc.lifesharing.validation.annotation.ExistMembers;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductsExistValidator implements ConstraintValidator<ExistMembers, Long> {

    private final ProductQueryService productQueryService;

    @Override
    public void initialize(ExistMembers constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<Product> target = productQueryService.findByProduct(value);
        if (target.isEmpty()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.MEMBER_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
