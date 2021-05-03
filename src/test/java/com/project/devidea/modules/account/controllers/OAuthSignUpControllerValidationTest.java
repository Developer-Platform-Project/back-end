package com.project.devidea.modules.account.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devidea.modules.account.AccountDummy;
import com.project.devidea.modules.account.dto.SignUp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
public class OAuthSignUpControllerValidationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void OAuth회원가입_기본Valid() throws Exception {

//        given
        SignUp.OAuthRequest failRequest = AccountDummy.getFailSignUpOAuthRequestWithValid();

//        when, then
//        provider 공백, email 공백, name 공백, provider 제공x, nickname 공백, id 공백
        mockMvc.perform(post("/sign-up/oauth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(failRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors.length()", is(6)));
    }

    @Test
    void OAuth회원가입_WithValidator() throws Exception {

//        given
        SignUp.OAuthRequest failRequest = AccountDummy.getFailSignUpOAuthRequestWithValidator();

//        when, then
//        지원하지 않는 provider, nickname 중복
        mockMvc.perform(post("/sign-up/oauth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(failRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors.length()", is(2)));
    }
}
