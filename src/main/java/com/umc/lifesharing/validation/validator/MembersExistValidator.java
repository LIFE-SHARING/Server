package com.umc.lifesharing.validation.validator;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.service.UserQueryService;
import com.umc.lifesharing.validation.annotation.ExistMembers;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MembersExistValidator implements ConstraintValidator<ExistMembers, Long> {

    private final UserQueryService userQueryService;

    @Override
    public void initialize(ExistMembers constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<User> target = userQueryService.findByUser(value);
        if (target.isEmpty()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.MEMBER_NOT_FOUND.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
