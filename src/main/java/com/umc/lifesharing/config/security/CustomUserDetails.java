package com.umc.lifesharing.config.security;

import com.umc.lifesharing.user.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {
    private final String id;    // DB에서 PK 값
    private final String email;        // 로그인용 ID 값
    private final String password;    // 비밀번호
    private final String name;    //닉네임
    private boolean emailVerified;    //이메일 인증 여부
    private boolean locked;    //계정 잠김 여부
    private Collection<GrantedAuthority> authorities;    //권한 목록

    public CustomUserDetails(Long id, String email, String password, String name) {
        this.id = id.toString();
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public CustomUserDetails(User user) {
        this.id = user.getId().toString();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
    }

    /**
     * 해당 유저의 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 비밀번호
     */
    @Override
    public String getPassword() {
        return password;
    }


    /**
     * PK값, eamil 중복 방지를 위해 pk를 넘긴다
     */
    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
