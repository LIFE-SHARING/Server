package com.umc.lifesharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories

public class LifeSharingApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifeSharingApplication.class, args);
    }

}
