package com.umc.lifesharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableJpaRepositories
@EnableJpaAuditing
@EnableCaching
@EnableFeignClients     //
public class LifeSharingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifeSharingApplication.class, args);
    }

}
