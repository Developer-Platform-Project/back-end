package com.project.devidea.modules.account;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.infra.error.GlobalResponse;
import com.project.devidea.modules.account.dto.*;
import com.project.devidea.modules.account.services.AccountService;
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

    @DeleteMapping("/account/quit")
    @ApiOperation("회원탈퇴")
    public ResponseEntity<?> quit(@AuthenticationPrincipal LoginUser loginUser) {

        accountService.quit(loginUser);
        return new ResponseEntity<>(GlobalResponse.of(), HttpStatus.OK);
    }
}
