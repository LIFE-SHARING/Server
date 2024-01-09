package com.umc.lifesharing.user.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

public class UserRequestDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class JoinDTO {
        @NotEmpty(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        private String email;

        @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
        @Length(min = 8, max = 16)
        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{8,16}$", message = "비밀번호는 8~16자의 영문 대소문자, 숫자, 특수문자로 이루어져야 합니다.")
        private String password;
    }


}
