package com.umc.lifesharing.config.security;

import com.umc.lifesharing.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserAdapter extends CustomUserDetails {
    @NotNull
    private User user;

    public UserAdapter (User user) {
        super(user);
        this.user = user;
    }

    public UserAdapter (User user, SocialLogin socialLogin) {
        super(user);
        this.user = user;
        this.socialLogin = socialLogin;
    }
}
