package com.project.devidea.modules.account.services.login;

import com.project.devidea.modules.account.dto.Login;

import java.util.Map;

public interface LoginService {

    String OAUTH_PASSWORD = "dev_idea_oauth_password";

    default boolean isOAuthType(Login.Request request) {
        return request instanceof Login.OAuth;
    }

    Map<String, String> login(Login.Request request) throws Exception;

}
