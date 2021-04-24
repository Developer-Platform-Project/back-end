package com.project.devidea.modules.account;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.infra.error.GlobalResponse;
import com.project.devidea.modules.account.dto.*;
import com.project.devidea.modules.account.service.AccountService;
import com.project.devidea.modules.account.validator.SignUpOAuthRequestValidator;
import com.project.devidea.modules.account.validator.SignUpRequestValidator;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final SignUpRequestValidator signUpRequestValidator;
    private final SignUpOAuthRequestValidator signUpOAuthRequestValidator;

    @InitBinder("commonRequest")
    public void initSignUpValidator(WebDataBinder binder) {
        binder.addValidators(signUpRequestValidator);
    }

    @InitBinder("OAuthRequest")
    public void initSignUpOAuthValidator(WebDataBinder binder) {
        binder.addValidators(signUpOAuthRequestValidator);
    }

    @PostMapping("/sign-up")
    @ApiOperation("회원가입")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUp.CommonRequest commonRequest) {

        return new ResponseEntity<>(GlobalResponse.of(accountService.signUp(commonRequest)), HttpStatus.OK);
    }

    @GetMapping("/authenticate-email-token")
    @ApiOperation("일반 회원가입 메일 인증")
    public void authenticateEmailToken(String email, String token, HttpServletResponse response) throws IOException {

        String url = accountService.authenticateEmailToken(email, token);
        response.sendRedirect(url + "/sign-up/detail?token=" + token);
    }

    @PostMapping("/sign-up/oauth")
    @ApiOperation("회원가입 - OAuth")
    public ResponseEntity<?> signUpOAuth(@Valid @RequestBody SignUp.OAuthRequest oAuthRequest)
            throws NoSuchAlgorithmException {

        return new ResponseEntity<>(GlobalResponse.of(accountService.signUpOAuth(oAuthRequest)), HttpStatus.OK);
    }

    @PostMapping("/login")
    @ApiOperation("로그인")
    public ResponseEntity<?> login(@Valid @RequestBody Login.Common login) throws Exception {

        Map<String, String> result = accountService.login(login);
        return new ResponseEntity<>(GlobalResponse.of(getIsSavedDetails(result)),
                getHttpHeaders(result), HttpStatus.OK);
    }

    @PostMapping("/login/oauth")
    @ApiOperation("로그인 - OAuth")
    public ResponseEntity<?> loginOAuth(@Valid @RequestBody Login.OAuth login) throws Exception {
        // 디테일 입력했는지 여부 확인하기
        Map<String, String> result = accountService.loginOAuth(login);
        return new ResponseEntity<>(GlobalResponse.of(getIsSavedDetails(result)),
                getHttpHeaders(result), HttpStatus.OK);
    }

    private Login.Response getIsSavedDetails(Map<String, String> result) {
        return Login.Response.builder()
                .savedDetail(Boolean.parseBoolean(result.get("savedDetail")))
                .emailCheckToken(result.get("emailCheckToken")).build();
    }

    private HttpHeaders getHttpHeaders(Map<String, String> result) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(result.get("header"), result.get("token"));
        return headers;
    }

    @PostMapping("/sign-up/detail")
    @ApiOperation("회원가입 디테일")
    public ResponseEntity<?> signUpDetail(@Valid @RequestBody SignUp.DetailRequest detailRequest) {

        accountService.saveSignUpDetail(detailRequest);
        return new ResponseEntity<>(GlobalResponse.of(), HttpStatus.OK);
    }

    @DeleteMapping("/account/quit")
    @ApiOperation("회원탈퇴")
    public ResponseEntity<?> quit(@AuthenticationPrincipal LoginUser loginUser) {

        accountService.quit(loginUser);
        return new ResponseEntity<>(GlobalResponse.of(), HttpStatus.OK);
    }
}
