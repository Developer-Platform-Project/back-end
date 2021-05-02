package com.project.devidea.modules.account.services.login;

import com.project.devidea.modules.account.dto.Login;
import com.project.devidea.modules.account.util.LoginServiceUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.Map;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {

    @Mock AuthenticationManager authenticationManager;
    @Mock LoginServiceUtil loginServiceUtil;
    @InjectMocks LoginServiceImpl loginService;

    @Test
    void 일반회원_로그인() throws Exception {
        
        // given
        Login.Common request = mock(Login.Common.class);
        Map<String, String> tokenMap = mock(Map.class);
        Map<String, String> loginResponse = mock(Map.class);
        when(loginServiceUtil.createToken(request.getEmail())).thenReturn(tokenMap);
        when(loginServiceUtil.createLoginResponse(tokenMap, request.getEmail()))
                .thenReturn(loginResponse);

        // when
        loginService.login(request);

        // then
        verify(authenticationManager).authenticate(any());
        verify(loginServiceUtil).createToken(request.getEmail());
        verify(loginServiceUtil).createLoginResponse(tokenMap, request.getEmail());
    }

    @Test
    void OAuth회원_로그인() throws Exception {

        // given
        Login.OAuth request = mock(Login.OAuth.class);
        Map<String, String> tokenMap = mock(Map.class);
        Map<String, String> loginResponse = mock(Map.class);
        when(loginServiceUtil.createToken(request.getEmail())).thenReturn(tokenMap);
        when(loginServiceUtil.createLoginResponse(tokenMap, request.getEmail()))
                .thenReturn(loginResponse);

        // when
        loginService.login(request);

        // then
        verify(authenticationManager).authenticate(any());
        verify(loginServiceUtil).createToken(request.getEmail());
        verify(loginServiceUtil).createLoginResponse(tokenMap, request.getEmail());
    }
}
