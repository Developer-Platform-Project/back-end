package com.project.devidea.modules.community;


import com.project.devidea.infra.TestConfig;
import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.AccountDummy;
import com.project.devidea.modules.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestConfig.class)
public class CommunityRepositoryTest {

    @Autowired
    CommunityRepository communityRepository;
    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void init() {
        Account account = accountRepository.save(AccountDummy.getAccount());
        communityRepository.saveAll(CommunityDummy.getCommunities(account));
    }

    @Test
    @DisplayName("특정 회원이 작성한 커뮤니티 글 전체 조회")
    public void findByAllAccounts() throws Exception {
        //given : 특정 회원정보로 작성된 커뮤니티 글 3개 존재
        Optional<Account> account = accountRepository.findByEmail(AccountDummy.getAccount().getEmail());

        //when : 회원정보를 통해 해당 회원이 작성한 커뮤니티 글들 전체 조회
        List<Community> findCommunities = communityRepository.findByWriter(account.get());

        //then : 3개의 커뮤니티 정상 조회 확인
        String[] titles = new String[] {"커뮤니티제목1","커뮤니티제목2","커뮤니티제목3"};
        String[] contents = new String[] {"커뮤니티내용1","커뮤니티내용2","커뮤니티내용3"};
        assertAll(
                () -> assertThat(findCommunities.size()).isEqualTo(3),
                () -> findCommunities.forEach(community -> {
                    assertThat(titles).contains(community.getTitle());
                    assertThat(contents).contains(community.getContent());
                })
        );
    }
}
