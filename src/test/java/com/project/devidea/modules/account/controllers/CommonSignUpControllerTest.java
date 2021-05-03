package com.project.devidea.modules.account.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devidea.infra.SHA256;
import com.project.devidea.modules.account.AccountDummy;
import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
class CommonSignUpControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired AccountRepository accountRepository;

    @Test
    void 일반_회원가입() throws Exception {

//        given
        SignUp.CommonRequest request = SignUp.CommonRequest.builder().name("고범떡").email("kob@naver.com")
                .password(SHA256.encrypt("123412341234")).passwordConfirm(SHA256.encrypt("123412341234"))
                .gender("male").nickname("고범떡").build();

//        when
        mockMvc.perform(post("/sign-up")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is("고범떡")))
                .andExpect(jsonPath("$.data.email", is("kob@naver.com")))
                .andExpect(jsonPath("$.data.gender", is("male")))
                .andExpect(jsonPath("$.data.nickname", is("고범떡")));

//        then
        assertEquals(request.getPassword(), SHA256.encrypt("123412341234"));
    }

    @Test
    void 회원가입_메일_받은_후_인증메일_확인() throws Exception {

//        given
        SignUp.CommonRequest join = AccountDummy.getSignUpRequest();
        mockMvc.perform(post("/sign-up")
                .content(objectMapper.writeValueAsString(join))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        Account account = accountRepository.findByEmail(join.getEmail()).get();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", account.getEmail());
        params.add("token", account.getEmailCheckToken());

//        when
        mockMvc.perform(get("/authenticate-email-token").params(params))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost:3000/sign-up/detail?token=" +
                        account.getEmailCheckToken()));

//        then
        Account find = accountRepository.findByEmail(join.getEmail()).get();
        assertTrue(find.isAuthenticateEmail());
    }

    @Test
    void 일반회원가입_기본Valid() throws Exception {

//        given
        SignUp.CommonRequest failValidSignUpRequest = AccountDummy.getFailSignUpRequestWithValid();

//        when, then
//        이메일 공백, password Blank, passwordConfirm Blank, name 길이, nickname Blank
        mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(failValidSignUpRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors.length()", is(5)));
    }
}