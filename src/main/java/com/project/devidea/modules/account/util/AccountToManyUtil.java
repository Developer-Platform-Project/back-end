package com.project.devidea.modules.account.util;

import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.domains.Interest;
import com.project.devidea.modules.account.domains.MainActivityZone;
import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.zone.Zone;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class AccountToManyUtil {

    public Set<Interest> createInterestSet(Account account, List<Tag> tags){
        Set<Interest> interests = new HashSet<>();
        tags.forEach(tag -> {
            interests.add(Interest.builder().tag(tag).account(account).build());
        });
        return interests;
    }

    public Set<MainActivityZone> createMainActivityZoneSet(Account account, List<Zone> zones) {
        Set<MainActivityZone> mainActivityZones = new HashSet<>();
        zones.forEach(zone -> {
            mainActivityZones.add(MainActivityZone.builder().account(account).zone(zone).build());
        });
        return mainActivityZones;
    }
}
