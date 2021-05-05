package com.project.devidea.modules.account.services.info;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.domains.Interest;
import com.project.devidea.modules.account.domains.MainActivityZone;
import com.project.devidea.modules.account.dto.Update;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.account.repository.InterestRepository;
import com.project.devidea.modules.account.repository.MainActivityZoneRepository;
import com.project.devidea.modules.account.util.AccountToManyUtil;
import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.tag.TagRepository;
import com.project.devidea.modules.tagzone.zone.Zone;
import com.project.devidea.modules.tagzone.zone.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateAccountInfoServiceImpl implements UpdateAccountInfoService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TagRepository tagRepository;
    private final InterestRepository interestRepository;
    private final ZoneRepository zoneRepository;
    private final MainActivityZoneRepository mainActivityZoneRepository;
    private final AccountToManyUtil accountToManyUtil;

    @Override
    public void profile(LoginUser loginUser, Update.ProfileRequest request) {
        Account account = accountRepository.findByEmail(loginUser.getUsername()).orElseThrow();
        account.updateProfile(request);
    }

    @Override
    public void password(LoginUser loginUser, Update.PasswordRequest request) {
        Account account = accountRepository.findByEmail(loginUser.getUsername()).orElseThrow();
        account.updatePassword("{bcrypt}" + passwordEncoder.encode(request.getPassword()));
    }

    @Override
    public void interest(LoginUser loginUser, Update.Interest request) {
        Account account = accountRepository.findByEmailWithInterests(loginUser.getUsername());
        List<Tag> tags = tagRepository.findByFirstNameIn(request.getInterests());
        Set<Interest> interests = accountToManyUtil.createInterestSet(account, tags);

//        기존에 있던 정보들 삭제
        interestRepository.deleteByAccount(account);

//        연관관계 메서드
        account.updateInterests(interests);
        interestRepository.saveAll(interests);
    }

    @Override
    public void mainActivityZones(LoginUser loginUser, Update.MainActivityZone request) {
        Account account = accountRepository.findByEmailWithMainActivityZones(loginUser.getUsername());
        Map<String, List<String>> map = request.splitCityAndProvince();
        List<Zone> zones
                = zoneRepository.findByCityInAndProvinceIn(map.get("city"), map.get("province"));
        Set<MainActivityZone> mainActivityZones = accountToManyUtil.createMainActivityZoneSet(account, zones);

//        기존에 있던 정보들 삭제
        mainActivityZoneRepository.deleteByAccount(account);

//        연관관계 메서드
        account.updateMainActivityZones(mainActivityZones);
        mainActivityZoneRepository.saveAll(mainActivityZones);
    }

    @Override
    public void nickname(LoginUser loginUser, Update.NicknameRequest request) {
        Account account = accountRepository.findByEmail(loginUser.getUsername()).orElseThrow();
        account.changeNickname(request.getNickname());
    }

    @Override
    public void notification(LoginUser loginUser, Update.Notification request) {
        Account account = accountRepository.findByEmail(loginUser.getUsername()).orElseThrow();
        account.updateNotifications(request);
    }

    @Override
    public void quit(LoginUser loginUser) {
        Account account = accountRepository.findByEmail(loginUser.getUsername()).orElseThrow();
        account.changeToQuit();
    }
}
