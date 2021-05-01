package com.project.devidea.modules.account.util;

import com.project.devidea.modules.account.dto.Login;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LoginResponseUtil {

    public Login.Response getLoginResponse(Map<String, String> response) {
        if (response.get("savedDetail").equals("false")) {
            return Login.Response.builder()
                    .savedDetail(false)
                    .emailCheckToken(response.get("emailCheckToken"))
                    .build();
        }
        return Login.Response.builder().savedDetail(true).emailCheckToken("").build();
    }

    public HttpHeaders getJwtHeader(Map<String, String> response) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(response.get("header"), response.get("token"));
        return headers;
    }
}
