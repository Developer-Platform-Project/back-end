package com.project.devidea.modules.account.controllers;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.infra.error.GlobalResponse;
import com.project.devidea.modules.account.dto.Update;
import com.project.devidea.modules.account.services.info.GetAccountInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account/settings")
public class GetAccountInfoController {

    private final GetAccountInfoService getAccountInfoService;

    @GetMapping("/profile")
    @ApiOperation("프로필 조회")
    public ResponseEntity<?> getProfile(@AuthenticationPrincipal LoginUser loginUser) {

        return new ResponseEntity<>(GlobalResponse.of(getAccountInfoService.profile(loginUser)), HttpStatus.OK);
    }

    @GetMapping("/interests")
    @ApiOperation("관심기술 조회")
    public ResponseEntity<?> getInterests(@AuthenticationPrincipal LoginUser loginUser) {

        return new ResponseEntity<>(GlobalResponse.of(getAccountInfoService.interests(loginUser)), HttpStatus.OK);
    }

    @GetMapping("/mainactivityzones")
    @ApiOperation("활동지역 조회")
    public ResponseEntity<?> getMainActivityZones(@AuthenticationPrincipal LoginUser loginUser) {

        return new ResponseEntity<>(GlobalResponse.of(getAccountInfoService.mainActivityZones(loginUser)), HttpStatus.OK);
    }

    @GetMapping("/nickname")
    @ApiOperation("닉네임 조회")
    public ResponseEntity<?> getAccountNickname(@AuthenticationPrincipal LoginUser loginUser) {

        Map<String, String> map = getAccountInfoService.nickname(loginUser);
        return new ResponseEntity<>(GlobalResponse.of(map), HttpStatus.OK);
    }

    @GetMapping("/notifications")
    @ApiOperation("알림설정 조회")
    public ResponseEntity<?> getAccountNotifications(@AuthenticationPrincipal LoginUser loginUser) {

        Update.Notification response = getAccountInfoService.notification(loginUser);
        return new ResponseEntity<>(GlobalResponse.of(response), HttpStatus.OK);
    }
}
