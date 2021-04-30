package com.project.devidea.modules.account.services.interest;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.Interest;
import com.project.devidea.modules.account.repository.InterestRepository;
import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.tag.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InterestServiceImplTest {

    @Mock TagService tagService;
    @Mock InterestRepository interestRepository;
    @InjectMocks InterestServiceImpl interestServiceImpl;

    @Test
    void 회원의_관심사_리스트_가져오기() throws Exception {

        // given
        Account account = mock(Account.class);
        List<String> interests = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        when(tagService.findInterests(interests)).thenReturn(tags);

        // when
        interestServiceImpl.getInterests(interests, account);

        // then
        verify(tagService).findInterests(interests);
    }

    @Test
    void 회원_관심사_저장() throws Exception {

        // given
        Set<Interest> interests = new HashSet<>();

        // when
        interestServiceImpl.saveAll(interests);

        // then
        verify(interestRepository).saveAll(interests);
    }
}