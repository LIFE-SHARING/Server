package com.umc.lifesharing.product.validation.validator;

import com.umc.lifesharing.product.entity.Product;
import com.umc.lifesharing.product.service.ProductQueryService;
import com.umc.lifesharing.product.validation.annotation.ExistProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductExistValidator implements ConstraintValidator<ExistProduct, Long> {

    private final ProductQueryService productQueryService;

    @Override
    public void initialize(ExistProduct constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<Product> isValid = productQueryService.findByProduct(value);

        if (isValid.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.PRODUCT_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
