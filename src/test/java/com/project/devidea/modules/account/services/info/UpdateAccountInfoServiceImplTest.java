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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UpdateAccountInfoServiceImplTest {

    @Mock AccountRepository accountRepository;
    @Mock BCryptPasswordEncoder passwordEncoder;
    @Mock TagRepository tagRepository;
    @Mock InterestRepository interestRepository;
    @Mock ZoneRepository zoneRepository;
    @Mock MainActivityZoneRepository mainActivityZoneRepository;
    @Mock AccountToManyUtil accountToManyUtil;
    @InjectMocks UpdateAccountInfoServiceImpl updateAccountInfoService;

    @Test
    void 프로필_업데이트() throws Exception {

//        given
        LoginUser loginUser = mock(LoginUser.class);
        Account account = mock(Account.class);
        Update.ProfileRequest request =
                mock(Update.ProfileRequest.class);
        when(accountRepository.findByEmail(account.getEmail()))
                .thenReturn(Optional.of(account));

//        when
        updateAccountInfoService.profile(loginUser, request);

//        then
        verify(accountRepository).findByEmail(account.getEmail());
        verify(account).updateProfile(request);
    }

    @Test
    void 패스워드_변경() throws Exception {

//        given
        LoginUser loginUser = mock(LoginUser.class);
        Account account = mock(Account.class);
        Update.PasswordRequest request = mock(Update.PasswordRequest.class);
        String changePassword = "{bcrypt}1234";
        when(passwordEncoder.encode(any()))
                .thenReturn(changePassword);
        when(accountRepository.findByEmail(account.getEmail()))
                .thenReturn(Optional.of(account));

//        when
        updateAccountInfoService.password(loginUser, request);

//        then
        verify(accountRepository).findByEmail(account.getEmail());
        verify(request).getPassword();
        verify(passwordEncoder).encode(request.getPassword());
        verify(account).updatePassword(any());
    }
    @Test
    void 관심기술_수정하기() throws Exception {

//        given
        LoginUser loginUser = mock(LoginUser.class);
        Account account = mock(Account.class);
        Update.Interest request = mock(Update.Interest.class);
        List<Tag> tags = new ArrayList<>();
        Tag tag = mock(Tag.class);
        tags.add(tag);
        Set<Interest> interestSet = mock(Set.class);

        when(accountRepository.findByEmailWithInterests(loginUser.getUsername()))
                .thenReturn(account);
        when(tagRepository.findByFirstNameIn(request.getInterests()))
                .thenReturn(tags);
        when(accountToManyUtil.createInterestSet(account, tags))
                .thenReturn(interestSet);


//        when
        updateAccountInfoService.interest(loginUser, request);

//        then
        verify(accountRepository).findByEmailWithInterests(loginUser.getUsername());
        verify(tagRepository).findByFirstNameIn(request.getInterests());
        verify(interestRepository).deleteByAccount(account);
        verify(account).updateInterests(interestSet);
        verify(interestRepository).saveAll(interestSet);
    }


    @Test
    void 활동지역_수정하기() throws Exception {

//        given
        LoginUser loginUser = mock(LoginUser.class);
        Account account = mock(Account.class);
        Update.MainActivityZone request =
                mock(Update.MainActivityZone.class);
        List<Zone> zones = new ArrayList<>();
        Zone zone = mock(Zone.class);
        Set<MainActivityZone> mainActivityZoneSet = mock(Set.class);

        zones.add(zone);

        when(accountRepository.findByEmailWithMainActivityZones(loginUser.getUsername()))
                .thenReturn(account);
        when(zoneRepository.findByCityInAndProvinceIn(any(), any()))
                .thenReturn(zones);
        when(accountToManyUtil.createMainActivityZoneSet(account, zones))
                .thenReturn(mainActivityZoneSet);

//        when
        updateAccountInfoService.mainActivityZones(loginUser, request);

//        then
        verify(accountRepository).findByEmailWithMainActivityZones(loginUser.getUsername());
        verify(request).splitCityAndProvince();
        verify(zoneRepository).findByCityInAndProvinceIn(any(), any());
        verify(mainActivityZoneRepository).deleteByAccount(account);
        verify(account).updateMainActivityZones(mainActivityZoneSet);
        verify(mainActivityZoneRepository).saveAll(mainActivityZoneSet);
    }

    @Test
    void 닉네임_변경하기() throws Exception{

//        given
        LoginUser loginUser = mock(LoginUser.class);
        String username = "username";
        String changeUsername = "changeUsername";
        Account account = mock(Account.class);
        Update.NicknameRequest request = mock(Update.NicknameRequest.class);
        when(loginUser.getUsername()).thenReturn(username);
        when(request.getNickname()).thenReturn(changeUsername);
        when(accountRepository.findByEmail(username))
                .thenReturn(Optional.of(account));

//        when
        updateAccountInfoService.nickname(loginUser, request);

//        then
        verify(loginUser).getUsername();
        verify(account).changeNickname(request.getNickname());
    }

    @Test
    void 알림_설정_수정하기() throws Exception {

//        given
        LoginUser loginUser = mock(LoginUser.class);
        Account account = mock(Account.class);
        Update.Notification request = mock(Update.Notification.class);
        when(loginUser.getUsername()).thenReturn("email");
        when(accountRepository.findByEmail(loginUser.getUsername()))
                .thenReturn(Optional.of(account));


//        when
        updateAccountInfoService.notification(loginUser, request);

//        then
        verify(accountRepository).findByEmail(loginUser.getUsername());
        verify(account).updateNotifications(request);
    }

    @Test
    void 회원_탈퇴() throws Exception {

//        given
        LoginUser loginUser = mock(LoginUser.class);
        Account account = mock(Account.class);
        String email = "email";
        when(loginUser.getUsername()).thenReturn(email);
        when(accountRepository.findByEmail(any())).thenReturn(Optional.of(account));

//        when
        updateAccountInfoService.quit(loginUser);

//        then
        verify(accountRepository).findByEmail(any());
        verify(account).changeToQuit();
    }
}