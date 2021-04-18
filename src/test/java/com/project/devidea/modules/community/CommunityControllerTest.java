package com.project.devidea.modules.community;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devidea.infra.MockMvcTest;
import com.project.devidea.infra.config.security.CustomUserDetailService;
import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.infra.config.security.jwt.JwtTokenUtil;
import com.project.devidea.infra.error.exception.EntityNotFoundException;
import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.AccountDummy;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.community.form.RequestCommunity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ValidationException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
public class CommunityControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    CustomUserDetailService customUserDetailService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void init() {
        Account account = accountRepository.save(AccountDummy.getAccount());
    }

    private Class<? extends Exception> getApiResultExceptionClass(MvcResult result) {
        return Objects.requireNonNull(result.getResolvedException()).getClass();
    }

    @Test
    @DisplayName("커뮤니티 글 생성 성공")
    public void createCommunity() throws Exception {
        //given : 로그인한 유저와 생성하고자 하는 커뮤니티 글이 존재
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername(AccountDummy.getAccount().getEmail());
        RequestCommunity requestCommunity = RequestCommunity.builder()
                .title("테스트제목")
                .content("테스트내용")
                .communityType("FREE").build();

        //when :  post, /community 으로 요청 시 새로운 커뮤니티 글 생성
        MvcResult mvcResult = mockMvc.perform(post("/community")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser))
                .content(objectMapper.writeValueAsString(requestCommunity))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then : 요청한 내용을 토대로 정상적으로 커뮤니티 글 생성 확인
        String strResult = mvcResult.getResponse().getContentAsString();
        Community createdCommunity = objectMapper.readValue(strResult, Community.class);
        assertAll(
                () -> assertThat(createdCommunity.getTitle()).isEqualTo("테스트제목"),
                () -> assertThat(createdCommunity.getContent()).isEqualTo("테스트내용"),
                () -> assertThat(createdCommunity.getCommunityType()).isEqualTo(CommunityType.FREE)
        );
    }

    @Test
    @DisplayName("커뮤니티 글 생성 실패_유효성 위반_문자열")
    public void createCommunity_fail_valid_string() throws Exception {
        //given : 로그인한 유저와 생성하고자 하는 커뮤니티 글이 존재(반드시 입력해야하는 문자열 title 값의 부재)
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername(AccountDummy.getAccount().getEmail());
        RequestCommunity requestCommunity = RequestCommunity.builder()
                .content("테스트내용")
                .communityType("FREE").build();

        //when : post, /community 으로 요청 시 새로운 커뮤니티 글 생성
        //then : 문자열 title 부재로 인한 validation 위반으로 4xx 에러, MethodArgumentNotValidException 발생 확인
        MvcResult mvcResult = mockMvc.perform(post("/community")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser))
                .content(objectMapper.writeValueAsString(requestCommunity))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertThat(getApiResultExceptionClass(result)).isEqualTo(MethodArgumentNotValidException.class)
                )
                .andReturn();
    }

    @Test
    @DisplayName("커뮤니티 글 생성 실패_유효성 위반_열거형")
    public void createCommunity_fail_valid_enum() throws Exception {
        //given : 로그인한 유저와 생성하고자 하는 커뮤니티 글이 존재(요청으로 넘어온 communityType 값과 매칭되는 열거형이 존재하지 않음)
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername(AccountDummy.getAccount().getEmail());
        RequestCommunity requestCommunity = RequestCommunity.builder()
                .title("테스트제목")
                .content("테스트내용")
                .communityType("없는 타입")
                .build();

        //when : post, /community 으로 요청 시 새로운 커뮤니티 글 생성
        //then : 잘못된 형태의 communityType 요청으로 인한 validation 위반으로 4xx 에러, MethodArgumentNotValidException 발생 확인
        MvcResult mvcResult = mockMvc.perform(post("/community")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser))
                .content(objectMapper.writeValueAsString(requestCommunity))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertThat(getApiResultExceptionClass(result)).isEqualTo(MethodArgumentNotValidException.class)
                )
                .andReturn();
    }
}
