package com.project.devidea.modules.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devidea.infra.SHA256;
import com.project.devidea.infra.config.security.CustomUserDetailService;
import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.infra.config.security.jwt.JwtTokenUtil;
import com.project.devidea.modules.account.dto.Login;
import com.project.devidea.modules.account.dto.SignUp;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class AccountControllerValidationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired JwtTokenUtil jwtTokenUtil;
    @Autowired CustomUserDetailService customUserDetailService;

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

    @Test
    void 일반회원가입_WithValidator() throws Exception {

//        given
        SignUp.CommonRequest commonRequest = AccountDummy.getFailSignUpRequestWithValidator();

//        when, then
//        이메일 중복과 패스워드, 패스워드 확인값 불일치, 닉네임 중복
        mockMvc.perform(post("/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commonRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors.length()", is(3)));
    }

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
    void 회원가입_상세정보_저장_토큰없음_경력년도가_음수인_경우() throws Exception {

//        given
        SignUp.DetailRequest failRequest = AccountDummy.getFailSignUpDetailRequestWithValid();

//        when, then
//        경력년도 음수, 토큰 없음
        mockMvc.perform(post("/sign-up/detail").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(failRequest)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.errors.length()", is(2)));
    }

    @Test
    @WithUserDetails("quit@quit.com")
    void 이미_탈퇴한_회원() throws Exception {

//        given
        LoginUser user =
                (LoginUser) customUserDetailService.loadUserByUsername("quit@quit.com");

//        when
        mockMvc.perform(delete("/account/quit")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
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
