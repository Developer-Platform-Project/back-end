package com.project.devidea.modules.account.services.signUp;

public interface AuthenticationEmailService {

    String authenticateEmailToken(String email, String token);
}
