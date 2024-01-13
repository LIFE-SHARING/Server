package com.umc.lifesharing.config.security;

import com.umc.lifesharing.user.entity.SocialLogin;
import com.umc.lifesharing.user.entity.User;
import jakarta.persistence.GeneratedValue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class UserAdapter extends CustomUserDetails {
    @NotNull
    private User user;
    private SocialLogin socialLogin;

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
