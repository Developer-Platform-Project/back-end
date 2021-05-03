package com.project.devidea.modules.community;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devidea.infra.MockMvcTest;
import com.project.devidea.infra.config.security.CustomUserDetailService;
import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.infra.config.security.jwt.JwtTokenUtil;
import com.project.devidea.infra.error.exception.EntityNotFoundException;
import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.AccountDummy;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.community.form.RequestCommunity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@MockMvcTest
public class CommunityControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    CustomUserDetailService customUserDetailService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CommunityRepository communityRepository;

    @BeforeEach
    void init() {
        Account account = accountRepository.save(AccountDummy.getAccount());
        communityRepository.saveAll(CommunityDummy.getCommunities(account));
    }

    private Class<? extends Exception> getApiResultExceptionClass(MvcResult result) {
        return Objects.requireNonNull(result.getResolvedException()).getClass();
    }

    @Test
    @DisplayName("커뮤니티 글 생성 성공")
    public void createCommunity() throws Exception {
        //given : 로그인한 유저와 생성하고자 하는 커뮤니티 글이 존재
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername(AccountDummy.getAccount().getEmail());
        RequestCommunity requestCommunity = RequestCommunity.builder()
                .title("테스트제목")
                .content("테스트내용")
                .communityType("FREE").build();

        //when :  post, /community 으로 요청 시 새로운 커뮤니티 글 생성
        MvcResult mvcResult = mockMvc.perform(post("/community")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser))
                .content(objectMapper.writeValueAsString(requestCommunity))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then : 요청한 내용을 토대로 정상적으로 커뮤니티 글 생성 확인
        String strResult = mvcResult.getResponse().getContentAsString();
        Community createdCommunity = objectMapper.readValue(strResult, Community.class);
        assertAll(
                () -> assertThat(createdCommunity.getTitle()).isEqualTo("테스트제목"),
                () -> assertThat(createdCommunity.getContent()).isEqualTo("테스트내용"),
                () -> assertThat(createdCommunity.getCommunityType()).isEqualTo(CommunityType.FREE)
        );
    }

    @Test
    @DisplayName("커뮤니티 글 생성 실패_유효성 위반_문자열")
    public void createCommunity_fail_valid_string() throws Exception {
        //given : 로그인한 유저와 생성하고자 하는 커뮤니티 글이 존재(반드시 입력해야하는 문자열 title 값의 부재)
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername(AccountDummy.getAccount().getEmail());
        RequestCommunity requestCommunity = RequestCommunity.builder()
                .content("테스트내용")
                .communityType("FREE").build();

        //when : post, /community 으로 요청 시 새로운 커뮤니티 글 생성
        //then : 문자열 title 부재로 인한 validation 위반으로 4xx 에러, MethodArgumentNotValidException 발생 확인
        MvcResult mvcResult = mockMvc.perform(post("/community")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser))
                .content(objectMapper.writeValueAsString(requestCommunity))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertThat(getApiResultExceptionClass(result)).isEqualTo(MethodArgumentNotValidException.class)
                )
                .andReturn();
    }

    @Test
    @DisplayName("커뮤니티 글 생성 실패_유효성 위반_열거형")
    public void createCommunity_fail_valid_enum() throws Exception {
        //given : 로그인한 유저와 생성하고자 하는 커뮤니티 글이 존재(요청으로 넘어온 communityType 값과 매칭되는 열거형이 존재하지 않음)
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername(AccountDummy.getAccount().getEmail());
        RequestCommunity requestCommunity = RequestCommunity.builder()
                .title("테스트제목")
                .content("테스트내용")
                .communityType("없는 타입")
                .build();

        //when : post, /community 으로 요청 시 새로운 커뮤니티 글 생성
        //then : 잘못된 형태의 communityType 요청으로 인한 validation 위반으로 4xx 에러, MethodArgumentNotValidException 발생 확인
        MvcResult mvcResult = mockMvc.perform(post("/community")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser))
                .content(objectMapper.writeValueAsString(requestCommunity))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(result ->
                        assertThat(getApiResultExceptionClass(result)).isEqualTo(MethodArgumentNotValidException.class)
                )
                .andReturn();
    }

    @Test
    @DisplayName("커뮤니티 글 전체 조회")
    public void getCommunities() throws Exception {
        //given : 커뮤니티 글 3개 존재
        //when : get, /community 으로 요청 시 커뮤니티 글 전체 조회
        MvcResult mvcResult = mockMvc.perform(get("/community"))
                .andExpect(status().isOk())
                .andReturn();

        //then : 3개의 커뮤니티 글 정상 조회 확인
        String strResult = mvcResult.getResponse().getContentAsString();
        List<Community> findCommunities = objectMapper.readValue(strResult, new TypeReference<List<Community>>() {});

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

    @Test
    @DisplayName("커뮤니티 글 상세 조회")
    public void getCommunityDetail() throws Exception {
        //given : 작성되어 있는 커뮤니티 글 1개 존재
        List<Community> communities = communityRepository.findByWriter(accountRepository.findByEmail(AccountDummy.getAccount().getEmail()).get());
        Community community = communities.get(0);

        //when : get, /community/{id} 으로 요청 시 특정 커뮤니티 글 조회
        MvcResult mvcResult = mockMvc.perform(get("/community/" + community.getId()))
                .andExpect(status().isOk())
                .andReturn();

        //then : id를 통해 찾은 커뮤니티 글이 찾고자하는 커뮤니티 글과 같은 지 확인
        String strResult = mvcResult.getResponse().getContentAsString();
        Community findCommunity = objectMapper.readValue(strResult, Community.class);

        assertAll(
                () -> assertThat(community.getTitle()).isEqualTo(findCommunity.getTitle()),
                () -> assertThat(community.getContent()).isEqualTo(findCommunity.getContent()),
                () -> assertThat(community.getId()).isEqualTo(findCommunity.getId())
        );
    }

    @Test
    @DisplayName("커뮤니티 글 상세 조회_실패_존재하지 않는 커뮤니티 글")
    public void getCommunityDetail_fail() throws Exception {
        //given : 존재하지 않는 커뮤니티 id
        Long id = Long.MAX_VALUE;

        //when : get, /community/{id} 으로 요청 시 특정 커뮤니티 글 조회
        //then : 존재하지 않는 id를 통한 커뮤니티 조회 요청으로 EntityNotFoundException 발생 확인
        MvcResult mvcResult = mockMvc.perform(get("/community/" + id))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> {
                    assertThat(getApiResultExceptionClass(result)).isEqualTo(EntityNotFoundException.class);
                })
                .andReturn();
    }

    @Test
    @DisplayName("커뮤니티 글 삭제")
    public void deleteCommunity() throws Exception {
        //given : 작성되어 있는 커뮤니티 글 1개 존재
        List<Community> communities = communityRepository.findByWriter(accountRepository.findByEmail(AccountDummy.getAccount().getEmail()).get());
        Community community = communities.get(0);
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername(AccountDummy.getAccount().getEmail());

        //when : post, /community/{id}/delete 으로 요청 시 특정 커뮤니티 글 삭제
        mockMvc.perform(post("/community/" + community.getId() + "/delete")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser)))
                .andExpect(status().isOk())
                .andReturn();

        //then : 커뮤니티 글 삭제 확인
        Optional<Community> findCommunity = communityRepository.findById(community.getId());
        assertFalse(findCommunity.isPresent());
    }

    @Test
    @DisplayName("커뮤니티 글 삭제_실패_존재하지 않는 커뮤니티 글")
    public void deleteCommunity_fail() throws Exception {
        //given : 존재하지 않는 커뮤니티 id
        Long id = Long.MAX_VALUE;
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername(AccountDummy.getAccount().getEmail());

        //when : get, /community/{id}/delete 으로 요청 시 특정 커뮤니티 글 삭제
        //then : 존재하지 않는 id를 통한 커뮤니티 삭제 요청으로 EntityNotFoundException 발생 확인
        mockMvc.perform(post("/community/" + id + "/delete")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> {
                    assertThat(getApiResultExceptionClass(result)).isEqualTo(EntityNotFoundException.class);
        });
    }

    @Test
    @DisplayName("커뮤니티 글 삭제_실패_권한없음(작성자 본인이 아님)")
    public void deleteCommunity_fail_accessDenied() throws Exception {
        //given : 작성되어있는 커뮤니티 글과, 글 작성자 본인이 아닌 다른 사용자 존재
        List<Community> communities = communityRepository.findByWriter(accountRepository.findByEmail(AccountDummy.getAccount().getEmail()).get());
        Community community = communities.get(0);

        Account account = Account.builder()
            .nickname("존재하지않는 새로운 닉네임")
            .name("이름")
            .password("123")
            .email("newEmail@naver.com")
            .roles("ROLE_USER")
            .build();
        accountRepository.saveAndFlush(account);
        LoginUser loginUser =
                (LoginUser) customUserDetailService.loadUserByUsername(account.getEmail());

        //when : get, /community/{id}/delete 으로 요청 시 특정 커뮤니티 글 삭제
        //then : 작성자 본인이 아닌 커뮤니티 글 삭제 요청으로 AccessDeniedException 발생 확인
        mockMvc.perform(post("/community/" + community.getId() + "/delete")
                .header("Authorization", "Bearer " + jwtTokenUtil.generateToken(loginUser)))
                .andExpect(status().is4xxClientError())
                .andExpect(result -> {
                    assertThat(getApiResultExceptionClass(result)).isEqualTo(AccessDeniedException.class);
                });
    }
}
