package com.project.devidea.modules.account.services.signUpDetail;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.Interest;
import com.project.devidea.modules.account.MainActivityZone;
import com.project.devidea.modules.account.dto.SignUp;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.account.services.interest.InterestService;
import com.project.devidea.modules.account.services.mainActivityZone.MainActivityZoneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignUpDetailServiceImplTest {

    @Mock AccountRepository accountRepository;
    @Mock MainActivityZoneService mainActivityZoneService;
    @Mock InterestService interestService;
    @InjectMocks SignUpDetailServiceImpl signUpDetailServiceImpl;

    @Test
    void 회원가입_디테일_저장() throws Exception {
        // given
        Account account = mock(Account.class);
        SignUp.DetailRequest request = mock(SignUp.DetailRequest.class);
        when(accountRepository.findByTokenWithMainActivityZoneAndInterests(request.getToken()))
                .thenReturn(account);
        Set<MainActivityZone> mainActivityZones = new HashSet<>();
        when(mainActivityZoneService.getMainActivityZones(request.splitCitiesAndProvinces(), account))
                .thenReturn(mainActivityZones);
        Set<Interest> interests = new HashSet<>();
        when(interestService.getInterests(request.getInterests(), account))
                .thenReturn(interests);

        // when
        signUpDetailServiceImpl.saveSignUpDetail(request);

        // then
        verify(accountRepository).findByTokenWithMainActivityZoneAndInterests(request.getToken());
        verify(mainActivityZoneService).getMainActivityZones(request.splitCitiesAndProvinces(), account);
        verify(interestService).getInterests(request.getInterests(), account);
        verify(account).saveSignUpDetail(request, mainActivityZones, interests);
        verify(mainActivityZoneService).saveAll(mainActivityZones);
        verify(interestService).saveAll(interests);
    }
}