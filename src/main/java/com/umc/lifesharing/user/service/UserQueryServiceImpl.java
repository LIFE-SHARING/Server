package com.umc.lifesharing.user.service;

import com.umc.lifesharing.apiPayload.code.status.ErrorStatus;
import com.umc.lifesharing.apiPayload.exception.GeneralException;
import com.umc.lifesharing.apiPayload.exception.handler.UserHandler;
import com.umc.lifesharing.config.security.CustomUserDetails;
import com.umc.lifesharing.config.security.UserAdapter;
import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserQueryServiceImpl implements UserQueryService  {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = validUserByEmail(email);

        return new UserAdapter(user);
    }

    // email(username)로 user를 찾는 메서드
    public User validUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).get();
        if(user == null)
            throw new UsernameNotFoundException(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
        return user;
    }
}
