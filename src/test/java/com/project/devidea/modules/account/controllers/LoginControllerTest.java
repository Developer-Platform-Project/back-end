package com.project.devidea.modules.account.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devidea.infra.SHA256;
import com.project.devidea.infra.config.security.jwt.JwtTokenUtil;
import com.project.devidea.modules.account.AccountDummy;
import com.project.devidea.modules.account.dto.Login;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
class LoginControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired JwtTokenUtil jwtTokenUtil;
    @Autowired AccountRepository accountRepository;

    @Test
    void 일반_로그인_디테일을_입력받지_못한_회원() throws Exception {

//        when, then
        MockHttpServletResponse mockHttpServletResponse = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Login.Common.builder()
                        .email("test2@test.com").password("1234").build())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.savedDetail", is(false)))
                .andExpect(jsonPath("$.data.emailCheckToken", notNullValue()))
                .andExpect(header().exists("Authorization")).andReturn().getResponse();

        String jwtToken = mockHttpServletResponse.getHeader("Authorization").substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        assertEquals(username, "test2@test.com");
    }

    @Test
    void OAuth_로그인_디테일을_입력받지_못한_회원() throws Exception {

//        given
        SignUp.OAuthRequest join = AccountDummy.getSignUpOAuthRequestDto2();
        mockMvc.perform(post("/sign-up/oauth").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(join))).andDo(print());

        Login.OAuth login = Login.OAuth.builder()
                .provider(join.getProvider()).id(join.getId()).email(join.getEmail()).build();

//        when
        MockHttpServletResponse response = mockMvc.perform(post("/login/oauth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(jsonPath("$.data.savedDetail", is(false)))
                .andExpect(jsonPath("$.data.emailCheckToken", notNullValue()))
                .andReturn().getResponse();

//        then, 토큰과 디테일을 입력받지 못한 회원임을 확인
        String jwtToken = response.getHeader("Authorization").substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        assertEquals(username, join.getEmail());
    }

    @Test
    void OAuth_로그인_디테일이_입력된_회원() throws Exception {

//        given
        SignUp.OAuthRequest join = AccountDummy.getSignUpOAuthRequestDto3();
//        회원가입
        mockMvc.perform(post("/sign-up/oauth").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(join))).andDo(print());
//        디테일 추가
        SignUp.DetailRequest detail = AccountDummy.getSignUpDetailRequestDto();
        String token = accountRepository.findByEmail(join.getEmail()).get().getEmailCheckToken();
        detail.setToken(token);
        mockMvc.perform(post("/sign-up/detail").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(detail)))
                .andDo(print());

        Login.OAuth login = Login.OAuth.builder()
                .provider(join.getProvider()).id(join.getId()).email(join.getEmail()).build();

//        when
        MockHttpServletResponse response = mockMvc.perform(post("/login/oauth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(jsonPath("$.data.savedDetail", is(true)))
                .andReturn().getResponse();

//        then, 토큰과 디테일을 입력받지 못한 회원임을 확인
        String jwtToken = response.getHeader("Authorization").substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        assertEquals(username, join.getEmail());
    }

}