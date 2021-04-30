package com.project.devidea.modules.account.services.mainActivityZone;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.MainActivityZone;
import com.project.devidea.modules.account.repository.MainActivityZoneRepository;
import com.project.devidea.modules.tagzone.zone.Zone;
import com.project.devidea.modules.tagzone.zone.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class MainActivityZoneServiceImpl implements MainActivityZoneService {

    private final ZoneService zoneService;
    private final MainActivityZoneRepository mainActivityZoneRepository;

    @Override
    public Set<MainActivityZone> getMainActivityZones(Map<String, List<String>> cityProvince, Account account) {
        List<Zone> zones = zoneService.findZones(cityProvince);
        return zonesToMainActivityZones(account, zones);
    }

    @Override
    public void saveAll(Set<MainActivityZone> mainActivityZones) {
        mainActivityZoneRepository.saveAll(mainActivityZones);
    }
}
