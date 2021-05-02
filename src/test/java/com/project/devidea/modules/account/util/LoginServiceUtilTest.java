package com.project.devidea.modules.account.util;

import com.project.devidea.infra.config.security.jwt.JwtTokenUtil;
import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceUtilTest {

    @Mock JwtTokenUtil jwtTokenUtil;
    @Mock AccountRepository accountRepository;
    @InjectMocks LoginServiceUtil loginServiceUtil;

    @Test
    void 토큰생성() throws Exception {

        // given
        String email = "email";
        String token = "token";
        Map<String, String> tokenMap = new HashMap<>();
        when(jwtTokenUtil.generateToken(email)).thenReturn(token);
        when(jwtTokenUtil.createTokenMap(token)).thenReturn(tokenMap);

        // when
        loginServiceUtil.createToken(email);

        // then
        verify(jwtTokenUtil).generateToken(email);
        verify(jwtTokenUtil).createTokenMap(token);
    }

    @Test
    void 로그인_응답_객체_생성() throws Exception {

        // given
        Map<String, String> response = new HashMap<>();
        String email = "email";
        Account account = mock(Account.class);
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(account));
        when(account.isSaveDetail()).thenReturn(true);

        // when
        loginServiceUtil.createLoginResponse(response, email);

        // then
        verify(accountRepository).findByEmail(email);
        verify(account).isSaveDetail();
    }

    @Test
    void 디테일이_저장되지_않은_회원_응답() throws Exception {

        // given
        Map<String, String> response = mock(Map.class);
        String emailCheckToken = "token";

        // when
        loginServiceUtil.createNotSaveDetailResponse(response, emailCheckToken);

        // then
        verify(response).put("savedDetail", "false");
        verify(response).put("emailCheckToken", emailCheckToken);
    }

    @Test
    void 디테일이_저장된_회원_응답() throws Exception {

        // given
        Map<String, String> response = mock(Map.class);

        // when
        loginServiceUtil.createSavedDetailResponse(response);

        // then
        verify(response).put("savedDetail", "true");
    }
}
