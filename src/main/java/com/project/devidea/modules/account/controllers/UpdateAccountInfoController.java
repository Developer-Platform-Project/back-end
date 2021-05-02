package com.project.devidea.modules.account.controllers;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.infra.error.GlobalResponse;
import com.project.devidea.modules.account.dto.Update;
import com.project.devidea.modules.account.services.info.UpdateAccountInfoService;
import com.project.devidea.modules.account.validator.NicknameValidator;
import com.project.devidea.modules.account.validator.PasswordValidator;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account/settings")
public class UpdateAccountInfoController {
    // TODO : 메서드명 가독성 좋게 변경하기
    private final UpdateAccountInfoService updateAccountInfoService;
    private final PasswordValidator updatePasswordValidator;
    private final NicknameValidator nicknameValidator;

    @InitBinder("passwordRequest")
    public void initUpdatePasswordValidator(WebDataBinder binder) {
        binder.addValidators(updatePasswordValidator);
    }

    @InitBinder("nicknameRequest")
    public void initNicknameValidator(WebDataBinder binder) {
        binder.addValidators(nicknameValidator);
    }

    @PatchMapping("/profile")
    @ApiOperation("프로필 수정")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal LoginUser loginUser,
                                           @RequestBody Update.ProfileRequest request) {

        updateAccountInfoService.profile(loginUser, request);
        return new ResponseEntity<>(GlobalResponse.of(), HttpStatus.OK);
    }

    @PatchMapping("/password")
    @ApiOperation("비밀번호 수정")
    public ResponseEntity<?> updatePassword(@AuthenticationPrincipal LoginUser loginUser,
                                            @Valid @RequestBody Update.PasswordRequest request) {

        updateAccountInfoService.password(loginUser, request);
        return new ResponseEntity<>(GlobalResponse.of(), HttpStatus.OK);
    }

    @PatchMapping("/interests")
    @ApiOperation("관심기술 수정")
    public ResponseEntity<?> updateInterests(@AuthenticationPrincipal LoginUser loginUser,
                                             @RequestBody Update.Interest request) {
        updateAccountInfoService.interest(loginUser, request);
        return new ResponseEntity<>(GlobalResponse.of(), HttpStatus.OK);
    }

    @PatchMapping("/mainactivityzones")
    @ApiOperation("활동지역 수정")
    public ResponseEntity<?> updateMainActivityZones(@AuthenticationPrincipal LoginUser loginUser,
                                                     @RequestBody Update.MainActivityZone request) {
        updateAccountInfoService.mainActivityZones(loginUser, request);
        return new ResponseEntity<>(GlobalResponse.of(), HttpStatus.OK);
    }

    @PatchMapping("/nickname")
    @ApiOperation("닉네임 수정")
    public ResponseEntity<?> updateAccountNickname(@AuthenticationPrincipal LoginUser loginUser,
                                                   @Valid @RequestBody Update.NicknameRequest request){

        updateAccountInfoService.nickname(loginUser, request);
        return new ResponseEntity<>(GlobalResponse.of(), HttpStatus.OK);
    }

    @PatchMapping("/notifications")
    @ApiOperation("알림설정 수정")
    public ResponseEntity<?> updateAccountNotifications(@AuthenticationPrincipal LoginUser loginUser,
                                                        @RequestBody Update.Notification request) {

        updateAccountInfoService.notification(loginUser, request);
        return new ResponseEntity<>(GlobalResponse.of(), HttpStatus.OK);
    }

    @DeleteMapping("/quit")
    @ApiOperation("회원탈퇴")
    public ResponseEntity<?> quit(@AuthenticationPrincipal LoginUser loginUser) {
        updateAccountInfoService.quit(loginUser);
        return new ResponseEntity<>(GlobalResponse.of(), HttpStatus.OK);
    }
}
