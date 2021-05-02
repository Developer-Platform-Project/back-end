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

    // TODO : api 수정하기, Update쪽으로 들어가는게 맞는거 같음!
    @DeleteMapping("/account/quit")
    @ApiOperation("회원탈퇴")
    public ResponseEntity<?> quit(@AuthenticationPrincipal LoginUser loginUser) {

//        accountService.quit(loginUser);
        return new ResponseEntity<>(GlobalResponse.of(), HttpStatus.OK);
    }
}
