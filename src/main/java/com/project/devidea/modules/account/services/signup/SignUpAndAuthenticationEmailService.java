package com.project.devidea.modules.account.services.signup;

public interface SignUpAndAuthenticationEmailService extends SignUpService{

    String authenticateEmailToken(String email, String token);
}
