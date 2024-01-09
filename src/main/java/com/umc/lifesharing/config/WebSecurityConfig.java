package com.umc.lifesharing.config;

import com.umc.lifesharing.user.service.UserQueryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // SpringSecurityFilterChain 자동 포함
@AllArgsConstructor     //  가능?
public class WebSecurityConfig {
    private UserQueryService userQueryService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    // TODO: 배포 전 설정 -> cors, reqMatchers 등
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .headers((headerConfig) ->  //  X-Frame-Options 헤더를 설정, 클릭 재킹과 같은 공격을 방지
                        headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers("/", "/login/**", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs",
                                        "*/**").permitAll()  // 임시
                                .anyRequest().authenticated()
                )
//                .exceptionHandling((exceptionConfig) ->
//                        exceptionConfig.authenticationEntryPoint(unauthorizedEntryPoint).accessDeniedHandler(accessDeniedHandler)
//                )
                .formLogin((formLogin) ->
                        formLogin
                                .loginPage("/login/page")  // 로그인 페이지의 URL을 지정
                                .usernameParameter("email")
                                .passwordParameter("password")
                                .loginProcessingUrl("/login")    // 로그인 요청시 URL을 지정
                                .defaultSuccessUrl("/", true)
                )
                .logout((logoutConfig) -> logoutConfig.logoutSuccessUrl("/"))
                .userDetailsService(userQueryService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}