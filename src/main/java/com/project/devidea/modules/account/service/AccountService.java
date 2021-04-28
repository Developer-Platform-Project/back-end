package com.project.devidea.modules.account.service;

import com.project.devidea.infra.config.security.oauth.OAuthService;
import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.Interest;
import com.project.devidea.modules.account.MainActivityZone;
import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.zone.Zone;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface AccountService extends OAuthService, AccountBasicService, AccountInfoService {


}
