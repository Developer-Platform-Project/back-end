package com.project.devidea.modules.account.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devidea.infra.SHA256;
import com.project.devidea.modules.account.AccountDummy;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
class OAuthSignUpControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    @DisplayName("OAuth 회원가입")
    void saveOAuthGoogle() throws Exception {

//        given
        SignUp.OAuthRequest signUpOAuthRequestDto = AccountDummy.getSignUpOAuthRequestDto();
        String encryptedId = signUpOAuthRequestDto.getId();

//        when
        mockMvc.perform(post("/sign-up/oauth")
                .content(objectMapper.writeValueAsString(signUpOAuthRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

//        then
        assertEquals(encryptedId, SHA256.encrypt("naver12341234"));
    }
}