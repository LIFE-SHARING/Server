package com.umc.lifesharing.temp.controller;

import com.umc.lifesharing.apiPayload.ApiResponse;
import com.umc.lifesharing.temp.dto.TempResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TempRestController {

    @GetMapping("/test")
    public ApiResponse<TempResponse.TempTestDTO> testAPI(){

        return ApiResponse.onSuccess(TempResponse.TempTestDTO.builder()
                .testString("This is Test!")
                .build());
    }

    @GetMapping("/health")
    public String healthChck() {
        return "health check!";
    }

    @GetMapping("/test2")
    public String test2() {
        return "test2";
    }
}