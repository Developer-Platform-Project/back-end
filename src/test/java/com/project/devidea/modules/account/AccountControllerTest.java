//package com.project.devidea.modules.account;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.project.devidea.infra.SHA256;
//import com.project.devidea.infra.config.security.CustomUserDetailService;
//import com.project.devidea.infra.config.security.LoginUser;
//import com.project.devidea.infra.config.security.jwt.JwtTokenUtil;
//import com.project.devidea.modules.account.domains.Account;
//import com.project.devidea.modules.account.domains.Interest;
//import com.project.devidea.modules.account.domains.MainActivityZone;
//import com.project.devidea.modules.account.dto.*;
//import com.project.devidea.modules.account.repository.AccountRepository;
//import com.project.devidea.modules.account.repository.InterestRepository;
//import com.project.devidea.modules.account.repository.MainActivityZoneRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.security.test.context.support.WithUserDetails;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.filter.CharacterEncodingFilter;
//
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.notNullValue;
//import static org.junit.Assert.assertTrue;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@WebAppConfiguration
//@AutoConfigureMockMvc
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Slf4j
//class AccountControllerTest {
//
//    @Autowired MockMvc mockMvc;
//    @Autowired WebApplicationContext webApplicationContext;
//    @Autowired ObjectMapper objectMapper;
//    @Autowired JwtTokenUtil jwtTokenUtil;
//    @Autowired CustomUserDetailService customUserDetailService;
//    @Autowired AccountRepository accountRepository;
//    @Autowired InterestRepository interestRepository;
//    @Autowired MainActivityZoneRepository mainActivityZoneRepository;
//
////    @BeforeEach
////    void preHandle() {
////        mockMvc = MockMvcBuilders
////                .webAppContextSetup(webApplicationContext)
////                .addFilters(new CharacterEncodingFilter("UTF-8", true))
////                .build();
////    }
////
////    @Test
////    void 일반_회원가입() throws Exception {
////
//////        given
////        SignUp.CommonRequest request = SignUp.CommonRequest.builder().name("고범떡").email("kob@naver.com")
////                .password(SHA256.encrypt("123412341234")).passwordConfirm(SHA256.encrypt("123412341234"))
////                .gender("male").nickname("고범떡").build();
////
//////        when
////        mockMvc.perform(post("/sign-up")
////                .content(objectMapper.writeValueAsString(request))
////                .contentType(MediaType.APPLICATION_JSON))
////                .andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.data.name", is("고범떡")))
////                .andExpect(jsonPath("$.data.email", is("kob@naver.com")))
////                .andExpect(jsonPath("$.data.gender", is("male")))
////                .andExpect(jsonPath("$.data.nickname", is("고범떡")));
////
//////        then
////        assertEquals(request.getPassword(), SHA256.encrypt("123412341234"));
////    }
////
////    @Test
////    void 회원가입_메일_받은_후_인증메일_확인() throws Exception {
////
//////        given
////        SignUp.CommonRequest join = AccountDummy.getSignUpRequest();
////        mockMvc.perform(post("/sign-up")
////                .content(objectMapper.writeValueAsString(join))
////                .contentType(MediaType.APPLICATION_JSON))
////                .andDo(print());
////
////        Account account = accountRepository.findByEmail(join.getEmail()).get();
////        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
////        params.add("email", account.getEmail());
////        params.add("token", account.getEmailCheckToken());
////
//////        when
////        mockMvc.perform(get("/authenticate-email-token").params(params))
////                .andDo(print())
////                .andExpect(status().is3xxRedirection())
////                .andExpect(redirectedUrl("http://localhost:3000/sign-up/detail?token=" +
////                        account.getEmailCheckToken()));
////
//////        then
////        Account find = accountRepository.findByEmail(join.getEmail()).get();
////        assertTrue(find.isAuthenticateEmail());
////    }
////
////    @Test
////    @DisplayName("OAuth 회원가입")
////    void saveOAuthGoogle() throws Exception {
////
//////        given
////        SignUp.OAuthRequest signUpOAuthRequestDto = AccountDummy.getSignUpOAuthRequestDto();
////        String encryptedId = signUpOAuthRequestDto.getId();
////
//////        when
////        mockMvc.perform(post("/sign-up/oauth")
////                .content(objectMapper.writeValueAsString(signUpOAuthRequestDto))
////                .contentType(MediaType.APPLICATION_JSON))
////                .andDo(print());
////
//////        then
////        assertEquals(encryptedId, SHA256.encrypt("naver12341234"));
////    }
////
////    @Test
////    void 일반_로그인_디테일을_입력받지_못한_회원() throws Exception {
////
//////        when, then
////        MockHttpServletResponse mockHttpServletResponse = mockMvc.perform(post("/login")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(objectMapper.writeValueAsString(Login.Common.builder()
////                        .email("test@test.com").password("1234").build())))
////                .andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(jsonPath("$.data.savedDetail", is(false)))
////                .andExpect(jsonPath("$.data.emailCheckToken", notNullValue()))
////                .andExpect(header().exists("Authorization")).andReturn().getResponse();
////
////        String jwtToken = mockHttpServletResponse.getHeader("Authorization").substring(7);
////        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
////        assertEquals(username, "test@test.com");
////    }
////
////    @Test
////    void OAuth_로그인_디테일을_입력받지_못한_회원() throws Exception {
////
//////        given
////        SignUp.OAuthRequest join = AccountDummy.getSignUpOAuthRequestDto2();
////        mockMvc.perform(post("/sign-up/oauth").contentType(MediaType.APPLICATION_JSON)
////                .content(objectMapper.writeValueAsString(join))).andDo(print());
////
////        Login.OAuth login = Login.OAuth.builder()
////                .provider(join.getProvider()).id(join.getId()).email(join.getEmail()).build();
////
//////        when
////        MockHttpServletResponse response = mockMvc.perform(post("/login/oauth")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(objectMapper.writeValueAsString(login)))
////                .andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(header().exists("Authorization"))
////                .andExpect(jsonPath("$.data.savedDetail", is(false)))
////                .andExpect(jsonPath("$.data.emailCheckToken", notNullValue()))
////                .andReturn().getResponse();
////
//////        then, 토큰과 디테일을 입력받지 못한 회원임을 확인
////        String jwtToken = response.getHeader("Authorization").substring(7);
////        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
////        assertEquals(username, join.getEmail());
////    }
////
////    @Test
////    void OAuth_로그인_디테일이_입력된_회원() throws Exception {
////
//////        given
////        SignUp.OAuthRequest join = AccountDummy.getSignUpOAuthRequestDto3();
//////        회원가입
////        mockMvc.perform(post("/sign-up/oauth").contentType(MediaType.APPLICATION_JSON)
////                .content(objectMapper.writeValueAsString(join))).andDo(print());
//////        디테일 추가
////        SignUp.DetailRequest detail = AccountDummy.getSignUpDetailRequestDto();
////        String token = accountRepository.findByEmail(join.getEmail()).get().getEmailCheckToken();
////        detail.setToken(token);
////        mockMvc.perform(post("/sign-up/detail").contentType(MediaType.APPLICATION_JSON)
////                .content(objectMapper.writeValueAsString(detail)))
////                .andDo(print());
////
////        Login.OAuth login = Login.OAuth.builder()
////                .provider(join.getProvider()).id(join.getId()).email(join.getEmail()).build();
////
//////        when
////        MockHttpServletResponse response = mockMvc.perform(post("/login/oauth")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(objectMapper.writeValueAsString(login)))
////                .andDo(print())
////                .andExpect(status().isOk())
////                .andExpect(header().exists("Authorization"))
////                .andExpect(jsonPath("$.data.savedDetail", is(true)))
////                .andReturn().getResponse();
////
//////        then, 토큰과 디테일을 입력받지 못한 회원임을 확인
////        String jwtToken = response.getHeader("Authorization").substring(7);
////        String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
////        assertEquals(username, join.getEmail());
////    }
////
////    @Test
////    @WithUserDetails(userDetailsServiceBeanName = "customUserDetailService", value = "test@test.com")
////    void 회원가입_상세정보_저장() throws Exception {
////
//////        given
////        LoginUser loginUser = (LoginUser) customUserDetailService.loadUserByUsername("test@test.com");
////        SignUp.DetailRequest request = AccountDummy.getSignUpDetailRequestDto();
////        request.setToken(loginUser.getAccount().getEmailCheckToken());
////
//////        when
////        mockMvc.perform(post("/sign-up/detail")
////                .contentType(MediaType.APPLICATION_JSON)
////                .content(objectMapper.writeValueAsString(request)))
////                .andDo(print());
////
//////        then account
////        Account account = accountRepository.findByEmailWithMainActivityZoneAndInterests(loginUser.getUsername());
////        assertAll(
////                () -> Assertions.assertTrue(account.isReceiveEmail()),
////                () -> assertEquals(account.getJob(), "웹개발"),
////                () -> assertEquals(account.getProfilePath(), "1234"),
////                () -> assertEquals(account.getTechStacks(), "java/python"),
////                () -> assertEquals(account.getInterests().size(), 3),
////                () -> assertEquals(account.getMainActivityZones().size(), 3));
////
//////        then mainActivityZone, interest
////        List<MainActivityZone> mainActivityZones = mainActivityZoneRepository.findByAccount(account);
////        List<Interest> interests = interestRepository.findByAccount(account);
////        assertAll(
////                () -> assertEquals(mainActivityZones.size(), 3),
////                () -> assertEquals(interests.size(), 3));
////    }
////    @Test
////    @WithUserDetails("test@test.com")
////    @Order(value = Integer.MAX_VALUE - 1)
////    void 회원_탈퇴() throws Exception {
////
//////        given
////        LoginUser user =
////                (LoginUser) customUserDetailService.loadUserByUsername("test@test.com");
////
//////        when
////        mockMvc.perform(delete("/account/quit")
////                .header("Authorization", jwtTokenUtil.generateToken(user))
////                .contentType(MediaType.APPLICATION_JSON))
////                .andDo(print())
////                .andExpect(status().isOk());
////
//////        then
////        LoginUser confirm =
////                (LoginUser) customUserDetailService.loadUserByUsername("test@test.com");
////        assertTrue(confirm.getAccount().isQuit());
////    }
//}
