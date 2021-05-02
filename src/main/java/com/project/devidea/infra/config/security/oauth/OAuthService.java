package com.project.devidea.infra.config.security.oauth;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.dto.Login;
import com.project.devidea.modules.account.dto.SignUp;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
// todo : 구조 리팩토링
public interface OAuthService {

    SignUp.Response signUpOAuth(SignUp.OAuthRequest request);

    Map<String, String> loginOAuth(Login.OAuth loginOAuthRequestDto) throws Exception;

    Account saveOAuthAccount(SignUp.OAuthRequest request);
}
