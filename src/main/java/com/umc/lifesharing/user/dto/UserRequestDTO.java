package com.umc.lifesharing.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
//        @Length(min = 8, max = 16)
//        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{8,16}$", message = "비밀번호는 8~16자의 영문 대소문자, 숫자, 특수문자로 이루어져야 합니다.")
        private String password;

        private String name;

        // TODO: - 포함 or 포함 x ?
        private String phone;

//        private Location location;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class LoginDTO {
        @NotEmpty(message = "이메일은 필수 입력 값입니다.")
        @Email(message = "이메일 형식이 아닙니다.")
        private String email;

        @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
//        @Length(min = 8, max = 16)
//        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{8,16}$", message = "비밀번호는 8~16자의 영문 대소문자, 숫자, 특수문자로 이루어져야 합니다.")
        private String password;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ChangePasswordDTO {
        @NotEmpty(message = "기존 비밀번호는 필수 입력 값입니다.")
//        @Length(min = 8, max = 16)
//        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{8,16}$", message = "비밀번호는 8~16자의 영문 대소문자, 숫자, 특수문자로 이루어져야 합니다.")
        private String oldPassword;

        @NotEmpty(message = "새 비밀번호는 필수 입력 값입니다.")
//        @Length(min = 8, max = 16)
//        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{8,16}$", message = "비밀번호는 8~16자의 영문 대소문자, 숫자, 특수문자로 이루어져야 합니다.")
        private String newPassword;

        // TODO: 확인 해야하나?..
//        @NotEmpty(message = "새 비밀번호는 확인은 필수 입력 값입니다.")
//        @Length(min = 8, max = 16)
//        @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*]{8,16}$", message = "비밀번호는 8~16자의 영문 대소문자, 숫자, 특수문자로 이루어져야 합니다.")
//        private String newPasswordConfirm;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class CheckNickname {
        @NotEmpty(message = "닉네임은 필수 입력 값입니다.")
        private String nickname;
    }

}
