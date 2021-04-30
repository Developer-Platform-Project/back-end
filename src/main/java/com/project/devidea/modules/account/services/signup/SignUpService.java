package com.project.devidea.modules.account.services.signup;

import com.project.devidea.modules.account.dto.SignUp;

public interface SignUpService {

    SignUp.Response signUp(SignUp.Request request);

}
