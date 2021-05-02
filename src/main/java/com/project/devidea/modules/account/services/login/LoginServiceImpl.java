package com.project.devidea.modules.account.services.login;

import com.project.devidea.infra.error.exception.ErrorCode;
import com.project.devidea.modules.account.dto.Login;
import com.project.devidea.modules.account.exception.AccountException;
import com.project.devidea.modules.account.util.LoginServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginServiceImpl implements LoginService {

    private final AuthenticationManager authenticationManager;
    private final LoginServiceUtil loginServiceUtil;

    @Override
    public Map<String, String> login(Login.Request request) throws Exception {
        if (isOAuthType(request)) {
            authenticate(request.getEmail(), OAUTH_PASSWORD);
        } else {
            authenticate(request.getEmail(), ((Login.Common) request).getPassword());
        }

        Map<String, String> tokenMap = loginServiceUtil.createToken(request.getEmail());
        return loginServiceUtil.createLoginResponse(tokenMap, request.getEmail());
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new DisabledException("이미 탈퇴한 회원입니다.", e);
        } catch (BadCredentialsException e) {
            throw new AccountException(ErrorCode.OK,
                    "가입되지 않거나, 메일과 비밀번호가 맞지 않습니다.",
                    Login.Response.builder().savedDetail(false).emailCheckToken(null).build());
        }
    }
}
