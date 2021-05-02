package com.project.devidea.modules.account.services;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.modules.account.dto.Login;
import com.project.devidea.modules.account.dto.SignUp;
import java.util.Map;

public interface AccountBasicService {

    // todo : OAuth, 일반 분리
    SignUp.Response signUp(SignUp.CommonRequest signUpRequestDto);

    // todo : OAuth, 일반 분리
    Map<String, String> login(Login.Common login) throws Exception;

    // todo : 로직 같음
    void saveSignUpDetail(SignUp.DetailRequest req);

    // todo : 일반 회원만
    String authenticateEmailToken(String email, String token);

    // todo : 로직 같음
    void quit(LoginUser loginUser);
}
