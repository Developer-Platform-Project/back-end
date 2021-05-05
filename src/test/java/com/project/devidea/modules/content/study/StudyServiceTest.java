package com.project.devidea.modules.content.study;

import com.project.devidea.infra.config.AppConfig;
import com.project.devidea.modules.ModuleGenerator;
import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.content.study.apply.StudyApplyRepository;
import com.project.devidea.modules.content.study.form.StudyDetailForm;
import com.project.devidea.modules.content.study.form.StudyMakingForm;
import com.project.devidea.modules.content.study.repository.StudyMemberRepository;
import com.project.devidea.modules.content.study.repository.StudyRepository;
import com.project.devidea.modules.content.study.service.StudyServiceImpl;
import com.project.devidea.modules.notification.NotificationRepository;
import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.tag.TagRepository;
import com.project.devidea.modules.tagzone.zone.Zone;
import com.project.devidea.modules.tagzone.zone.ZoneRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {AppConfig.class, StudyServiceImpl.class},
        webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Slf4j
public class StudyServiceTest {
    ModuleGenerator moduleGenerator;

    @SpyBean
    StudyFactory studyFactory;
    @SpyBean
    ModelMapper studyMapper;
    @MockBean
    ZoneRepository zoneRepository;
    @MockBean
    TagRepository tagRepository;

    @MockBean
    StudyRepository studyRepository;
    @MockBean
    AccountRepository accountRepository;
    @MockBean
    StudyApplyRepository studyApplyRepository;
    @MockBean
    StudyMemberRepository studyMemberRepository;

    @MockBean
    NotificationRepository notificationRepository;

    Study study;
    Account account1, account2;
    StudyMember studyMember1, studyMember2;

    @Autowired
    StudyServiceImpl studyService;
    String TITLE="송파스터디";
    Zone 서울송파구;
    Zone 서울강남구;
    Tag spring;
    Tag java;
    @BeforeEach
    void init() {
        Zone 서울송파구 = moduleGenerator.generateZone("서울특별시","송파구",1L);
        Zone 서울강남구 = moduleGenerator.generateZone("서울특별시","강남구",2L);
        Tag spring = moduleGenerator.generateTag("spring",1L);
        Tag java = moduleGenerator.generateTag("java",2L);
        account1 =moduleGenerator.generateAccount("account1",1L);
        account2 =moduleGenerator.generateAccount("account2",1L);

        when(zoneRepository.findAll()).thenReturn(Arrays.asList(서울송파구, 서울강남구));
        when(zoneRepository.findByCityAndProvince("서울", "송파구")).thenReturn(서울송파구);
        when(tagRepository.findAll()).thenReturn(Arrays.asList(spring, java));
        when(accountRepository.findAll()).thenReturn(Arrays.asList(account1, account2));
        when(accountRepository.findByNickname("account1")).thenReturn(account1);

        study = moduleGenerator.generateStudy(TITLE,new ArrayList<StudyMember>(),tagRepository.findAll(),서울송파구);
        studyMember1=moduleGenerator.generateStudyMember(study,account1,1L);
        studyMember2=moduleGenerator.generateStudyMember(study,account1,2L);
        when(studyMemberRepository.findAll()).thenReturn(Arrays.asList(studyMember1, studyMember2));
        study.setMembers(new HashSet<>(studyMemberRepository.findAll()));

    }

    @Test
    @DisplayName("스터디 상세 폼보기")
    void getDetailStudy() {
        //given
        when(studyRepository.findById(study.getId()))
                .thenReturn(Optional.ofNullable(study));
        //when
        StudyDetailForm studyDetailForm = studyService.getDetailStudy((study.getId()));
        assertAll(
                () -> assertNotNull(studyDetailForm),
                () -> assertEquals(studyDetailForm.getId(), study.getId()),
                () -> assertEquals(studyDetailForm.getMembers(), study.getMembers().stream()
                        .map(studyMember -> {
                            return studyMember.toString();
                        }).collect(Collectors.toSet())),
                () -> assertEquals(studyDetailForm.getTitle(), study.getTitle()),
                () -> assertEquals(studyDetailForm.getTags(), study.getTags().stream()
                        .map(tag -> {
                            return tag.toString();
                        }).collect(Collectors.toSet())),
                () -> assertEquals(studyDetailForm.getLocation(), study.getLocation().toString()),
                () -> assertEquals(studyDetailForm.getCounts(), study.getCounts())
        );
    }

    @Test
    @DisplayName("스터디 만들기")
    void makingStudy() {
        //given
        StudyMakingForm studyMakingForm = new StudyMakingForm();
        studyMakingForm.setOpen(true);
        studyMakingForm.setQuestion("question");
        studyMakingForm.setLocation("서울/송파구");
        studyMakingForm.setLevel(Level.입문);
        studyMakingForm.setMaxCount(6);
        studyMakingForm.setTitle(TITLE);
        studyMakingForm.setRecruiting(true);
        studyMakingForm.setTags(new HashSet<String>(tagRepository.findAll().stream()
                .map(tag -> {
                    return tag.toString();
                }).collect(Collectors.toSet())));
        //when
        StudyDetailForm studyDetailForm = studyService.makingStudy(account1, studyMakingForm);
        assertAll(
                () -> assertNotNull(studyDetailForm),
                () -> assertEquals(studyDetailForm.getMembers(), study.getMembers().stream()
                        .map(studyMember -> {
                            return studyMember.toString();
                        }).collect(Collectors.toSet())),
                () -> assertEquals(studyDetailForm.getTitle(), study.getTitle()),
                () -> assertEquals(studyDetailForm.getLocation(), study.getLocation().toString()),
                () -> assertEquals(studyDetailForm.getCounts(),1)
        );
    }



}
