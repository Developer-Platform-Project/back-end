package com.project.devidea.modules.account.services;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.modules.account.dto.Update;

import java.util.Map;
// todo : 구조 리팩토링
public interface AccountInfoService {

    Update.ProfileResponse getProfile(LoginUser loginUser);

    void updateProfile(LoginUser loginUser, Update.ProfileRequest request);

    void updatePassword(LoginUser loginUser, Update.PasswordRequest request);

    Update.Interest getAccountInterests(LoginUser loginUser);

    void updateAccountInterests(LoginUser loginUser, Update.Interest request);

    Update.MainActivityZone getAccountMainActivityZones(LoginUser loginUser);

    void updateAccountMainActivityZones(LoginUser loginUser, Update.MainActivityZone request);

    Map<String, String> getAccountNickname(LoginUser loginUser);

    void updateAccountNickname(LoginUser loginUser, Update.NicknameRequest request);

    Update.Notification getAccountNotification(LoginUser loginUser);

    void updateAccountNotification(LoginUser loginUser, Update.Notification request);

}
