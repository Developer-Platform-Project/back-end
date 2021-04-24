package com.project.devidea.modules.account.service;

import com.project.devidea.infra.config.security.oauth.OAuthService;

public interface AccountService extends OAuthService, AccountBasicService, AccountInfoService {
}
