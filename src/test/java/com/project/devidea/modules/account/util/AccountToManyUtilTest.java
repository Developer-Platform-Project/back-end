package com.project.devidea.modules.account.util;

import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.domains.Interest;
import com.project.devidea.modules.account.domains.MainActivityZone;
import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.zone.Zone;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
class AccountToManyUtilTest {

    @InjectMocks
    AccountToManyUtil util;

    @Test
    void 관심사_set으로_변환() throws Exception {

        // given
        Account account = Account.builder().build();
        List<Tag> tags = Arrays.asList(
                Tag.builder().firstName("javascript").secondName("자바스크립트").thirdName(null).build(),
                Tag.builder().firstName("python").secondName("파이썬").thirdName(null).build()
        );

        // when
        Set<Interest> interests = util.createInterestSet(account, tags);

        // then
        List <String> result = interests.stream()
                .map(interest -> interest.getTag().getFirstName())
                .collect(toList());
        assertEquals(result.size(), 2);
        assertThat(result).contains("javascript", "python");
    }

    @Test
    void 활동지역_set으로_변환() throws Exception {

        // given
        Account account = Account.builder().build();
        List<Zone> zones = Arrays.asList(
                Zone.builder().city("서울특별시").province("광진구").build(),
                Zone.builder().city("경기도").province("수원시").build()
        );

        // when
        Set<MainActivityZone> mainActivityZones = util.createMainActivityZoneSet(account, zones);

        // then
        List <String> result = mainActivityZones.stream()
                .map(zone -> zone.getZone().toString())
                .collect(toList());
        assertEquals(result.size(), 2);
        assertThat(result).contains("서울특별시/광진구", "경기도/수원시");
    }
}