package com.project.devidea.modules.account.util;

import com.project.devidea.modules.account.dto.Login;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LoginResponseUtilTest {

    @InjectMocks LoginResponseUtil util;

    @Test
    void login_response_반환_디테일이_저장되지_않은_회원() throws Exception {

        // given
        Map<String, String> map = new HashMap<>();
        map.put("savedDetail", "false");

        // when
        Login.Response response = util.getLoginResponse(map);

        // then
        assertFalse(response.isSavedDetail());
    }

    @Test
    void login_response_반환_디테일이_저장된_회원() throws Exception {

        // given
        Map<String, String> map = new HashMap<>();
        map.put("savedDetail", "true");

        // when
        Login.Response response = util.getLoginResponse(map);

        // then
        assertTrue(response.isSavedDetail());
    }

    @Test
    void jwt_헤더로_반환하기() throws Exception {

        // given
        Map<String, String> response = new HashMap<>();
        response.put("header", "header");
        response.put("token", "token");

        // when
        HttpHeaders httpHeaders = util.getJwtHeader(response);

        // then
        assertTrue(httpHeaders.get("header").contains("token"));
    }
}