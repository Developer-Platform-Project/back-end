package com.project.devidea.modules.account.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devidea.infra.SHA256;
import com.project.devidea.modules.account.dto.Login;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
public class LoginControllerValidationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void 일반로그인_기본Valid() throws Exception {

//        given
        Login.Common loginRequestDto = Login.Common.builder().email("asdfsdf").password("").build();

//        when, then
//        not email, empty password
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors.length()", is(2)));
    }

    @Test
    void 일반로그인_아이디_비밀번호가_일치하지_않은_경우() throws Exception {

//        given
        Login.Common loginRequestDto = Login.Common.builder().email("test@test.com").password("11").build();

//        when, then
//        아이디와 비밀번호가 일치하지 않을 때, BadCredentialException 발생
        mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("가입되지 않거나, 메일과 비밀번호가 맞지 않습니다.")));
    }

    @Test
    void OAuth로그인_기본Valid() throws Exception {

//        given
        Login.OAuth login = Login.OAuth.builder().provider("").id("").email("").build();

//        when, then
//        empty id
        mockMvc.perform(post("/login/oauth").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors.length()", is(3)));
    }

    @Test
    void OAuth로그인_미가입_회원의_경우() throws Exception {

//        given
        Login.OAuth login = Login.OAuth.builder().provider("google")
                .id(SHA256.encrypt("google12341234")).email("failEmail@gmail.com").build();

//        when, then
        mockMvc.perform(post("/login/oauth").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.savedDetail", is(false)))
                .andExpect(jsonPath("$.data.emailCheckToken", nullValue()));
    }

    @Test
    @WithUserDetails("quit@quit.com")
    void 탈퇴한_회원_로그인_실패() throws Exception {

//        given
        Login.Common login =
                Login.Common.builder().email("quit@quit.com").password(SHA256.encrypt("1234")).build();

//        when, then
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("이미 탈퇴한 회원입니다.")));
    }
}
