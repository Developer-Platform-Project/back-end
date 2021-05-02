package com.project.devidea.modules.account.controllers;

import com.project.devidea.infra.error.GlobalResponse;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.services.signUp.SignUpService;
import com.project.devidea.modules.account.validator.SignUpOAuthRequestValidator;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 *  OAuth 회원가입 로직
 */
@RestController
@RequiredArgsConstructor
public class OAuthSignUpController {

    private final SignUpService signUpService;
    private final SignUpOAuthRequestValidator signUpOAuthRequestValidator;

    @InitBinder("OAuthRequest")
    public void initSignUpOAuthValidator(WebDataBinder binder) {
        binder.addValidators(signUpOAuthRequestValidator);
    }

    //TODO : OAuth
    @PostMapping("/sign-up/oauth")
    @ApiOperation("회원가입 - OAuth")
    public ResponseEntity<?> signUpOAuth(@Valid @RequestBody SignUp.OAuthRequest oAuthRequest){
        return new ResponseEntity<>(GlobalResponse.of(signUpService.signUp(oAuthRequest)), HttpStatus.OK);
    }
}
