package com.umc.lifesharing.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@OpenAPIDefinition(servers = {@Server(url ="https://적용 시 해야 함./")},
//        info = @Info(title = "LifeSharing API 명세서",
//                description = "LifeSharing",
//                version = "v1"))
@OpenAPIDefinition(info = @Info(title = "LifeSharing API 명세서",
                description = "LifeSharing",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("LifeSharing API v1")
                .pathsToMatch(paths)
                .build();
    }
}