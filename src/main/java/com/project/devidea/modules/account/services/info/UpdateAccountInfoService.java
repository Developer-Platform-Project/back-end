package com.project.devidea.modules.account.services.info;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.modules.account.dto.Update;

public interface UpdateAccountInfoService {

    void profile(LoginUser loginUser, Update.ProfileRequest request);

    void password(LoginUser loginUser, Update.PasswordRequest request);

    void interest(LoginUser loginUser, Update.Interest request);

    void mainActivityZones(LoginUser loginUser, Update.MainActivityZone request);

    void nickname(LoginUser loginUser, Update.NicknameRequest request);

    void notification(LoginUser loginUser, Update.Notification request);

    void quit(LoginUser loginUser);
}
