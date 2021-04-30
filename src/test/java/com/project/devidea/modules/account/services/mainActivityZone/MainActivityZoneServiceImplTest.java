package com.project.devidea.modules.account.services.mainActivityZone;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.MainActivityZone;
import com.project.devidea.modules.account.repository.MainActivityZoneRepository;
import com.project.devidea.modules.tagzone.zone.Zone;
import com.project.devidea.modules.tagzone.zone.ZoneService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainActivityZoneServiceImplTest {

    @Mock ZoneService zoneService;
    @Mock MainActivityZoneRepository mainActivityZoneRepository;
    @InjectMocks MainActivityZoneServiceImpl mainActivityZoneServiceImpl;

    @Test
    void 회원활동지역_객체리스트_생성() throws Exception {

        // given
        Account account = mock(Account.class);
        Map<String, List<String>> cityProvince = new HashMap<>();
        List<Zone> zones = new ArrayList<>();
        when(zoneService.findZones(cityProvince))
                .thenReturn(zones);

        // when
        mainActivityZoneServiceImpl.getMainActivityZones(cityProvince, account);

        // then
        verify(zoneService).findZones(cityProvince);
    }

    @Test
    void 활동지역_저장() throws Exception {

        // given
        Set<MainActivityZone> mainActivityZoneSet = new HashSet<>();

        // when
        mainActivityZoneServiceImpl.saveAll(mainActivityZoneSet);

        // then
        verify(mainActivityZoneRepository).saveAll(mainActivityZoneSet);
    }
}