package com.project.devidea.modules.account.services.signUp;

public interface SignUpAndAuthenticationEmailService extends SignUpService{

    String authenticateEmailToken(String email, String token);
}
