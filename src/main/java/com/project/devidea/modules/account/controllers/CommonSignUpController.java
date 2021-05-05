package com.project.devidea.modules.account.controllers;

import com.project.devidea.infra.error.GlobalResponse;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.services.signUp.AuthenticationEmailService;
import com.project.devidea.modules.account.services.signUp.SignUpService;
import com.project.devidea.modules.account.validator.SignUpRequestValidator;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class CommonSignUpController {

    @Qualifier("commonSignUpService")
    private final SignUpService signUpService;
    private final AuthenticationEmailService authenticationEmailService;
    private final SignUpRequestValidator signUpRequestValidator;

    @InitBinder("commonRequest")
    public void initSignUpValidator(WebDataBinder binder) {
        binder.addValidators(signUpRequestValidator);
    }

    @PostMapping("/sign-up")
    @ApiOperation("회원가입")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUp.CommonRequest commonRequest) {

        return new ResponseEntity<>(GlobalResponse.of(signUpService.signUp(commonRequest)), HttpStatus.OK);
    }

    @GetMapping("/authenticate-email-token")
    @ApiOperation("일반 회원가입 메일 인증")
    public void authenticateEmailToken(String email, String token, HttpServletResponse response) throws IOException {

        String url = authenticationEmailService.authenticateEmailToken(email, token);
        response.sendRedirect(url + "/sign-up/detail?token=" + token);
    }
}
