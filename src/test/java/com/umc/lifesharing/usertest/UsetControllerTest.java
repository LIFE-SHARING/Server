package com.umc.lifesharing.usertest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.lifesharing.user.dto.UserRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UsetControllerTest {
    private String baseUrl = "http://localhost:8080";

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void 회원가입_테스트() throws Exception {
        // given
        String email = "test1@naver.com";
        String password = "1234";
        String name = "김테스트";
        String phone = "010123456778";

        UserRequestDTO.JoinDTO joinDTO = UserRequestDTO.JoinDTO.builder()
                .email(email)
                .password(password)
                .name(name)
                .phone(phone)
                .build();

        String url = baseUrl + "/auth/join";

        // when
        // object to json
        String body = mapper.writeValueAsString(joinDTO);

        //then
        mvc.perform(post(url)
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk());
//                .andExpect(content().string("1"));

    }
}
