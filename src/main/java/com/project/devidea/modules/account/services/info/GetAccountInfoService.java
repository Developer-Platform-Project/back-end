package com.project.devidea.modules.account.services.info;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.modules.account.dto.Update;

import java.util.Map;

public interface GetAccountInfoService {

    Update.ProfileResponse profile(LoginUser loginUser);

    Update.Interest interests(LoginUser loginUser);

    Update.MainActivityZone mainActivityZones(LoginUser loginUser);

    Map<String, String> nickname(LoginUser loginUser);

    Update.Notification notification(LoginUser loginUser);
}
