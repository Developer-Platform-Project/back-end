package com.project.devidea.modules.account.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devidea.infra.config.security.CustomUserDetailService;
import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.infra.config.security.jwt.JwtTokenUtil;
import com.project.devidea.modules.account.AccountDummy;
import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.domains.Interest;
import com.project.devidea.modules.account.domains.MainActivityZone;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.account.repository.InterestRepository;
import com.project.devidea.modules.account.repository.MainActivityZoneRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
class SignUpDetailControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired CustomUserDetailService customUserDetailService;
    @Autowired MainActivityZoneRepository mainActivityZoneRepository;
    @Autowired InterestRepository interestRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired JwtTokenUtil jwtTokenUtil;

    @Test
    @WithUserDetails("update@update.com")
    void 회원가입_상세정보_저장() throws Exception {

//        given
        LoginUser loginUser = (LoginUser) customUserDetailService.loadUserByUsername("update@update.com");
        SignUp.DetailRequest request = AccountDummy.getSignUpDetailRequestDto();
        request.setToken(loginUser.getAccount().getEmailCheckToken());

//        when
        mockMvc.perform(post("/sign-up/detail")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print());

//        then
        Account account = accountRepository.findByEmailWithMainActivityZoneAndInterests(loginUser.getUsername());
        assertAll(
                () -> Assertions.assertTrue(account.isReceiveEmail()),
                () -> assertEquals(account.getJob(), "웹개발"),
                () -> assertEquals(account.getProfilePath(), "1234"),
                () -> assertEquals(account.getTechStacks(), "java/python"),
                () -> assertEquals(account.getInterests().size(), 3),
                () -> assertEquals(account.getMainActivityZones().size(), 3));

//        then mainActivityZone, interest
        List<MainActivityZone> mainActivityZones = mainActivityZoneRepository.findByAccount(account);
        List<Interest> interests = interestRepository.findByAccount(account);
        assertAll(
                () -> assertEquals(mainActivityZones.size(), 3),
                () -> assertEquals(interests.size(), 3));
    }

}