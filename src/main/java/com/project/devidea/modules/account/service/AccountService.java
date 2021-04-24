package com.project.devidea.modules.account.service;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.infra.config.security.jwt.JwtTokenUtil;
import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.Interest;
import com.project.devidea.modules.account.MainActivityZone;
import com.project.devidea.modules.account.dto.Login;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.zone.Zone;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AccountService {

    default Map<String, String> createToken(String email, JwtTokenUtil tokenUtil){
        String jwtToken = tokenUtil.generateToken(email);
        return tokenUtil.createTokenMap(jwtToken);
    }

    default Set<MainActivityZone> getMainActivityZones(Account account, List<Zone> zones) {
        Set<MainActivityZone> mainActivityZones = new HashSet<>();
        zones.forEach(zone -> {
            mainActivityZones.add(MainActivityZone.builder().account(account).zone(zone).build());
        });
        return mainActivityZones;
    }

    default Set<Interest> getInterests(Account account, List<Tag> tags) {
        Set<Interest> interests = new HashSet<>();
        tags.forEach(tag -> {
            interests.add(Interest.builder().tag(tag).account(account).build());
        });
        return interests;
    }

    SignUp.Response signUp(SignUp.CommonRequest signUpRequestDto);

    Account saveAccount(SignUp.CommonRequest signUpRequestDto);

    Map<String, String> login(Login.Common login) throws Exception;

    void saveSignUpDetail(SignUp.DetailRequest req);

    String authenticateEmailToken(String email, String token);

    void quit(LoginUser loginUser);

}
