package com.project.devidea.modules.account.service.signup;

import com.project.devidea.modules.account.dto.SignUp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommonSignUpServiceImplTest {

    @InjectMocks
    SignUpService signUpService;

    @Test
    void 회원가입_일반() throws Exception {

//        given
        SignUp.SignUpRequest request = mock(SignUp.SignUpRequest.class);


//        when


//        then

    }
}