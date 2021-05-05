package com.project.devidea.modules.account.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devidea.infra.config.security.CustomUserDetailService;
import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.infra.config.security.jwt.JwtTokenUtil;
import com.project.devidea.modules.account.domains.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
class GetAccountInfoControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired CustomUserDetailService customUserDetailService;
    @Autowired JwtTokenUtil jwtTokenUtil;

    @Test
    @WithUserDetails(value = "update@update.com")
    void 유저의_프로필_가져오기() throws Exception {

    //        given
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername("update@update.com");

    //        when
        MvcResult result = mockMvc.perform(get("/account/settings/profile")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

    //        then
        Account account = loginUser.getAccount();
        String str = result.getResponse().getContentAsString();
        Map<String, String> map = objectMapper.readValue(str, Map.class);
        assertAll(() -> assertEquals(account.getEmail(), "update@update.com"),
                () -> assertEquals(account.getNickname(), "업뎃테스트"));
    }

    @Test
    @WithUserDetails(value = "test@test.com")
    void 관심기술_가져오기() throws Exception {

//        given
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername("test@test.com");

//        when, then
        mockMvc.perform(get("/account/settings/interests")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithUserDetails("test2@test.com")
    void 활동지역_가져오기() throws Exception {

//        given
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername("test2@test.com");

//        when, then
        mockMvc.perform(get("/account/settings/mainactivityzones")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.mainActivityZones.length()", is(0)));
    }

    @Test
    @WithUserDetails("test@test.com")
    void 닉네임_가져오기() throws Exception {

//        given
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername("test@test.com");

//        when, then
        mockMvc.perform(get("/account/settings/nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser)))
                .andDo(print())
                .andExpect(jsonPath("$.data.nickname", is(loginUser.getNickName())));
    }

    @Test
    @WithUserDetails("test@test.com")
    void 알림_설정_가져오기() throws Exception {

//        given
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername("test@test.com");

//        when, then
        mockMvc.perform(get("/account/settings/notifications")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()", is(6)))
                .andDo(print());
    }
}