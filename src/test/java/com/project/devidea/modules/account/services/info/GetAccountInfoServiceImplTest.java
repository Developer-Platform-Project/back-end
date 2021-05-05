package com.project.devidea.modules.account.services.info;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.modules.account.AccountDummy;
import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.dto.Update;
import com.project.devidea.modules.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAccountInfoServiceImplTest {

    @Mock ModelMapper modelMapper;
    @Mock AccountRepository accountRepository;
    @InjectMocks GetAccountInfoServiceImpl getAccountInfoService;

    @Test
    void 프로필_가져오기() throws Exception {

//        given
        LoginUser loginUser = mock(LoginUser.class);
        when(getAccountInfoService.profile(loginUser))
                .thenReturn(AccountDummy.getAccountProfileResponseDtoAtMockito());

//        when
        Update.ProfileResponse response =
                getAccountInfoService.profile(loginUser);

//        then
        verify(modelMapper).map(loginUser.getAccount(), Update.ProfileResponse.class);
    }

    @Test
    void 관심기술_가져오기() throws Exception {

//        given
        LoginUser loginUser = mock(LoginUser.class);
        Account account = mock(Account.class);
        when(accountRepository.findByEmailWithInterests(loginUser.getUsername())).thenReturn(account);

//        when
        getAccountInfoService.interests(loginUser);

//        then
        verify(accountRepository).findByEmailWithInterests(loginUser.getUsername());
    }

    @Test
    void 활동지역_가져오기() throws Exception {

//        given
        LoginUser loginUser = mock(LoginUser.class);
        Account account = mock(Account.class);
        when(accountRepository.findByEmailWithMainActivityZones(loginUser.getUsername()))
                .thenReturn(account);

//        when
        getAccountInfoService.mainActivityZones(loginUser);

//        then
        verify(accountRepository).findByEmailWithMainActivityZones(loginUser.getUsername());
    }

    @Test
    void 닉네임_가져오기() throws Exception {

//        given
        LoginUser loginUser = mock(LoginUser.class);
        Account account = mock(Account.class);
        String nickname = "1234";
        when(loginUser.getAccount()).thenReturn(account);
        when(account.getNickname()).thenReturn(nickname);

//        when
        getAccountInfoService.nickname(loginUser);

//        then
        verify(loginUser).getAccount();
        verify(account).getNickname();
    }

    @Test
    void 알림_설정_가져오기() throws Exception {

//        given
        LoginUser loginUser = mock(LoginUser.class);
        Account account = mock(Account.class);
        when(loginUser.getAccount()).thenReturn(account);

//        when
        getAccountInfoService.notification(loginUser);

//        then
        verify(loginUser).getAccount();
        verify(modelMapper).map(account, Update.Notification.class);
    }
}