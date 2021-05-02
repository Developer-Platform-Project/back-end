package com.project.devidea.modules.account.controllers;

import com.project.devidea.infra.error.GlobalResponse;
import com.project.devidea.modules.account.dto.Login;
import com.project.devidea.modules.account.services.login.LoginService;
import com.project.devidea.modules.account.util.LoginResponseUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RequestMapping("/login")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final LoginResponseUtil loginResponseUtil;

    @PostMapping("/")
    @ApiOperation("로그인")
    public ResponseEntity<?> login(@Valid @RequestBody Login.Common login) throws Exception {
        Map<String, String> response = loginService.login(login);

        return new ResponseEntity<>(GlobalResponse.of(loginResponseUtil.getLoginResponse(response)),
                loginResponseUtil.getJwtHeader(response),
                HttpStatus.OK);
    }

    @PostMapping("/oauth")
    @ApiOperation("로그인 - OAuth")
    public ResponseEntity<?> loginOAuth(@Valid @RequestBody Login.OAuth login) throws Exception {
        Map<String, String> response = loginService.login(login);

        return new ResponseEntity<>(GlobalResponse.of(loginResponseUtil.getLoginResponse(response)),
                loginResponseUtil.getJwtHeader(response),
                HttpStatus.OK);
    }
}
