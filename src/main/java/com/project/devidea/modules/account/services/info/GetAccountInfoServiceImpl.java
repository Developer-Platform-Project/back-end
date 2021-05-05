package com.project.devidea.modules.account.services.info;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.domains.Interest;
import com.project.devidea.modules.account.domains.MainActivityZone;
import com.project.devidea.modules.account.dto.Update;
import com.project.devidea.modules.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GetAccountInfoServiceImpl implements GetAccountInfoService {

    private final ModelMapper modelMapper;
    private final AccountRepository accountRepository;

    @Override
    public Update.ProfileResponse profile(LoginUser loginUser) {
        return modelMapper.map(loginUser.getAccount(), Update.ProfileResponse.class);
    }

    @Override
    public Update.Interest interests(LoginUser loginUser) {
        Set<Interest> interests =
                accountRepository.findByEmailWithInterests(loginUser.getUsername()).getInterests();
        return Update.Interest.builder().interests(
                interests.stream()
                        .map(interest -> interest.getTag().getFirstName())
                        .collect(Collectors.toList())).build();
    }

    @Override
    public Update.MainActivityZone mainActivityZones(LoginUser loginUser) {
        Set<MainActivityZone> mainActivityZones =
                accountRepository.findByEmailWithMainActivityZones(loginUser.getUsername()).getMainActivityZones();
        return Update.MainActivityZone.builder()
                .mainActivityZones(
                        mainActivityZones.stream().map(zone -> zone.getZone().toString()).collect(Collectors.toList()))
                .build();
    }

    @Override
    public Map<String, String> nickname(LoginUser loginUser) {
        Map<String, String> map = new HashMap<>();
        map.put("nickname", loginUser.getAccount().getNickname());
        return map;
    }

    @Override
    public Update.Notification notification(LoginUser loginUser) {
        Account account = loginUser.getAccount();
        return modelMapper.map(account, Update.Notification.class);
    }
}
