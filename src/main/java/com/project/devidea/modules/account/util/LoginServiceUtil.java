package com.project.devidea.modules.account.util;

import com.project.devidea.infra.config.security.jwt.JwtTokenUtil;
import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoginServiceUtil {

    private final JwtTokenUtil jwtTokenUtil;
    private final AccountRepository accountRepository;

    public Map<String, String> createToken(String email) {
        String token = jwtTokenUtil.generateToken(email);
        return jwtTokenUtil.createTokenMap(token);
    }

    public Map<String, String> createLoginResponse(Map<String, String> response, String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow();
        if (!account.isSaveDetail())
            return createNotSaveDetailResponse(response, account.getEmailCheckToken());

        return createSavedDetailResponse(response);
    }

    public Map<String, String> createSavedDetailResponse(Map<String, String> response) {
        response.put("savedDetail", "true");
        return response;
    }

    public Map<String, String> createNotSaveDetailResponse(Map<String, String> response, String emailCheckToken) {
        response.put("savedDetail", "false");
        response.put("emailCheckToken", emailCheckToken);
        return response;
    }
}
