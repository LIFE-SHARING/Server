package com.umc.lifesharing.user.service;

import com.umc.lifesharing.user.entity.User;
import com.umc.lifesharing.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String email = username;

        User user = validUserByEmail(email);

        return null;
    }

    public User validUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if(user == null)
            throw new UsernameNotFoundException(email);
        else
            return user;
    }
}
