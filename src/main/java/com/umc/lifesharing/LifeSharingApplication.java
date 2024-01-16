package com.umc.lifesharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LifeSharingApplication {
// 테스트
    public static void main(String[] args) {
        SpringApplication.run(LifeSharingApplication.class, args);
    }

}
