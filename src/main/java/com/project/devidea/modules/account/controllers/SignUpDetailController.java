package com.project.devidea.modules.account.controllers;

import com.project.devidea.infra.error.GlobalResponse;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.services.signUpDetail.SignUpDetailService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 *  회원가입 디테일 저장
 */
@RestController
@RequiredArgsConstructor
public class SignUpDetailController {

    private final SignUpDetailService signUpDetailService;

    @PostMapping("/sign-up/detail")
    @ApiOperation("회원가입 디테일")
    public ResponseEntity<?> signUpDetail(@Valid @RequestBody SignUp.DetailRequest detailRequest) {

        signUpDetailService.saveSignUpDetail(detailRequest);
        return new ResponseEntity<>(GlobalResponse.of(), HttpStatus.OK);
    }
}
