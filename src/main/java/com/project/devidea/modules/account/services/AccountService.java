package com.project.devidea.modules.account.services;

import com.project.devidea.infra.config.security.oauth.OAuthService;

public interface AccountService extends OAuthService, AccountBasicService, AccountInfoService {


}
