package com.project.devidea.modules.account.services.mainActivityZone;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.MainActivityZone;
import com.project.devidea.modules.tagzone.zone.Zone;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface MainActivityZoneService {

    Set<MainActivityZone> getMainActivityZones(Map<String, List<String>> cityProvince, Account account);

    default Set<MainActivityZone> zonesToMainActivityZones(Account account, List<Zone> zones) {
        Set<MainActivityZone> mainActivityZones = new HashSet<>();
        zones.forEach(zone -> {
            mainActivityZones.add(MainActivityZone.builder().account(account).zone(zone).build());
        });
        return mainActivityZones;
    }

    void saveAll(Set<MainActivityZone> mainActivityZones);
}
