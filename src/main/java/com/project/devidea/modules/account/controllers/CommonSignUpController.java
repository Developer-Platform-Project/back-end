package com.project.devidea.modules.account.controllers;

import com.project.devidea.infra.error.GlobalResponse;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.services.signup.SignUpAndAuthenticationEmailService;
import com.project.devidea.modules.account.validator.SignUpRequestValidator;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 일반 회원가입, 메일인증, 회원탈퇴 로직
 */
@RestController
@RequiredArgsConstructor
public class CommonSignUpController {

    private final SignUpAndAuthenticationEmailService signUpService;
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

        String url = signUpService.authenticateEmailToken(email, token);
        response.sendRedirect(url + "/sign-up/detail?token=" + token);
    }
}
