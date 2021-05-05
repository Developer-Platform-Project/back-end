package com.project.devidea.modules.community;

import com.project.devidea.infra.config.security.CustomUserDetailService;
import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.AccountDummy;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.community.form.RequestCommunity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class CommunityServiceTest {


    @Autowired
    CommunityService communityService;
    @Autowired
    CustomUserDetailService customUserDetailService;
    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void init() {
        Account account = accountRepository.save(AccountDummy.getAccount());
    }

    @Test
    @DisplayName("커뮤니티 글 생성_성공")
    public void createCommunity() throws Exception {
        //given : 로그인한 유저와 생성하고자 하는 커뮤니티 글이 존재
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername(AccountDummy.getAccount().getEmail());

        RequestCommunity requestCommunity = RequestCommunity.builder()
                .title("테스트제목")
                .content("테스트내용")
                .communityType("FREE").build();

        //when : 커뮤니티 글 생성
        Community createdCommunity = communityService.createCommunity(loginUser.getAccount(), requestCommunity);

        //then : 요청한 내용을 토대로 정상적으로 커뮤니티 글 생성 확인
        Assertions.assertAll(
                () -> assertThat(createdCommunity.getTitle()).isEqualTo("테스트제목"),
                () -> assertThat(createdCommunity.getContent()).isEqualTo("테스트내용"),
                () -> assertThat(createdCommunity.getCommunityType()).isEqualTo(CommunityType.FREE),
                () -> assertThat(createdCommunity.getWriter()).isEqualTo(loginUser.getAccount())
        );
    }
}
