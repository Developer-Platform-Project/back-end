package com.project.devidea.modules.account.service.signup;

import com.project.devidea.modules.account.dto.SignUp;

public interface SignUpService {

    SignUp.Response signUp(SignUp.SignUpRequest request);

}
