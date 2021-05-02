package com.project.devidea.modules.account.services.signUpDetail;

import com.project.devidea.modules.account.dto.SignUp;

public interface SignUpDetailService {

    void saveSignUpDetail(SignUp.DetailRequest request);
}
