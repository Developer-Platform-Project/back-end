package com.project.devidea.modules.content.suggestion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devidea.infra.MockMvcTest;
import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.content.mentoring.Mentee;
import com.project.devidea.modules.content.mentoring.MenteeRepository;
import com.project.devidea.modules.content.mentoring.Mentor;
import com.project.devidea.modules.content.mentoring.MentorRepository;
import com.project.devidea.modules.content.mentoring.account.WithAccount;
import com.project.devidea.modules.content.resume.Resume;
import com.project.devidea.modules.content.resume.ResumeRepository;
import com.project.devidea.modules.content.suggestion.form.SuggestionRequest;
import com.project.devidea.modules.notification.Notification;
import com.project.devidea.modules.notification.NotificationRepository;
import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.tag.TagRepository;
import com.project.devidea.modules.tagzone.zone.Zone;
import com.project.devidea.modules.tagzone.zone.ZoneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @Transactional
@MockMvcTest
public class SuggestionControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TagRepository tagRepository;
    @Autowired
    ZoneRepository zoneRepository;
    @Autowired
    MenteeRepository menteeRepository;
    @Autowired
    MentorRepository mentorRepository;
    @Autowired
    ResumeRepository resumeRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    SuggestionService suggestionService;

    @BeforeEach
    @Transactional
    public void init() throws IOException {

        Resource resource = null;
        if (tagRepository.count() == 0) {
            resource = new ClassPathResource("tag_kr.csv");
            Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8).stream()
                    .forEach(line -> {
                        String[] split = line.split(",");
                        Tag tag = Tag.builder()
                                .firstName(split[1])
                                .secondName(split[2].equals("null") ? null : split[2])
                                .thirdName(split[3].equals("null") ? null : split[3])
                                .build();
                        if (!split[0].equals("parent")) {
                            Tag tagParent = tagRepository.findByFirstName(split[0]);
                            tagParent.addChild(tag);
                        }
                        tagRepository.save(tag);
                    });
        }

        if (zoneRepository.count() == 0) {
            resource = new ClassPathResource("zones_kr.csv");
            Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8).stream()
                    .forEach(line -> {
                        String[] split = line.split(",");
                        Zone zone = Zone.builder()
                                .city(split[0])
                                .province(split[1])
                                .build();
                        zoneRepository.save(zone);
                    });
        }

        Account account1 = Account.builder()
                .nickname("account1")
                .email("account1@email.com")
                .bio("bio")
                .gender("남성")
                .roles("ROLE_USER")
                .name("account1")
                .password("{bcrypt}" + passwordEncoder.encode("1234"))
                .joinedAt(LocalDateTime.now())
                .build();

        Account account2 = Account.builder()
                .nickname("account2")
                .email("account2@email.com")
                .bio("bio")
                .gender("여성")
                .roles("ROLE_USER")
                .name("account2")
                .password("{bcrypt}" + passwordEncoder.encode("1234"))
                .joinedAt(LocalDateTime.now())
                .build();

        accountRepository.saveAll(Arrays.asList(account1, account2));

        // Mentee
        Set<String> tagSet1 = new HashSet<String>(Arrays.asList("java", "python", "scala"));
        Set<String> zoneSet1 = new HashSet<String>(Arrays.asList("경기도/수정구", "경기도/광주시"));
        Mentee mentee1 = Mentee.createMentee(account1,
                "멘티1 설명",
                getZones(zoneSet1),
                getTags(tagSet1),
                true);
        menteeRepository.save(mentee1);

        // Mentor
        Set<String> tagSet2 = new HashSet<String>(Arrays.asList("react", "AngularJS", "express", "flask"));
        Set<String> zoneSet2 = new HashSet<String>(Arrays.asList("서울특별시/강서구", "서울특별시/금천구", "서울특별시/구로구"));

        Resume resume = Resume.createResume(account2,
                "01012345678",
                "account2@github.com",
                "account2@blog.com");
        resumeRepository.save(resume);

        Mentor mentor = Mentor.createMentor(account2,
                resume,
                getZones(zoneSet2),
                getTags(tagSet2),
                false,
                10000);
        mentorRepository.save(mentor);
    }

    private Set<Tag> getTags(Set<String> tags) {
        return tags.stream()
                .map(tag -> tagRepository.findByFirstName(tag)).collect(Collectors.toSet());
    }

    private Set<Zone> getZones(Set<String> zones) {
        return zones.stream().map(zone -> {
            String[] locations = zone.split("/");
            return zoneRepository.findByCityAndProvince(locations[0], locations[1]);
        }).collect(Collectors.toSet());
    }

    private Resume createResume(Account account) {
        Resume resume = Resume.builder()
                .phoneNumber("01012345678")
                .github("yk@github.com")
                .blog("yk@blog.com")
                .account(account)
                .build();
        resumeRepository.save(resume);
        return resume;
    }

    @DisplayName("제안하기 - 로그인한 유저가 멘토일 때")
    @Test
    @WithAccount("yk")
    public void suggest_fromMentorToMentee() throws Exception {

        // Given
        Account account = accountRepository.findByNickname("yk");
        Resume resume = createResume(account);

        Set<String> tagSet = new HashSet<String>(Arrays.asList("algorithm", "kubernetes", "docker", "devops"));
        Set<String> zoneSet = new HashSet<String>(Arrays.asList("부산광역시/사하구", "부산광역시/북구", "부산광역시/금정구"));

        Mentor mentor = Mentor.createMentor(account,
                resume,
                getZones(zoneSet),
                getTags(tagSet),
                true,
                0);
        mentorRepository.save(mentor);

        // When, Then
        Account account1 = accountRepository.findByNickname("account1");
        Long menteeId = menteeRepository.findByAccountId(account1.getId()).getId();

        SuggestionRequest request = SuggestionRequest.builder()
                .message("message : yk -> account1")
                .build();

        String result = mockMvc.perform(post(String.format("/suggestion/%d", menteeId))
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long suggestionId = Long.parseLong(result);
        // 알림 조회
        List<Notification> notifications = notificationRepository.findByAccount(account1);
        assertEquals(1, notifications.size());
        assertEquals("새로운 제안이 도착했습니다.", notifications.get(0).getTitle());
        assertEquals("message : yk -> account1", notifications.get(0).getMessage());
    }

    @DisplayName("제안하기 - 로그인한 유저가 멘티일 때")
    @Test
    @WithAccount("yk")
    public void suggest_fromMenteeToMentor() throws Exception {

        // Given
        Account account = accountRepository.findByNickname("yk");

        Set<String> tagSet = new HashSet<String>(Arrays.asList("algorithm", "kubernetes", "docker", "devops"));
        Set<String> zoneSet = new HashSet<String>(Arrays.asList("부산광역시/사하구", "부산광역시/북구", "부산광역시/금정구"));

        Mentee mentee = Mentee.createMentee(account,
                "멘티 설명",
                getZones(zoneSet),
                getTags(tagSet),
                true);
        menteeRepository.save(mentee);

        // When
        // Then
        Account account2 = accountRepository.findByNickname("account2");
        Long mentorId = mentorRepository.findByAccountId(account2.getId()).getId();

        SuggestionRequest request = SuggestionRequest.builder()
                .message("message : yk -> account2")
                .build();

        String result = mockMvc.perform(post(String.format("/suggestion/%d", mentorId))
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Long suggestionId = Long.parseLong(result);

        List<Notification> notifications = notificationRepository.findByAccount(account2);
        assertEquals(1, notifications.size());
        assertEquals("새로운 제안이 도착했습니다.", notifications.get(0).getTitle());
        assertEquals("message : yk -> account2", notifications.get(0).getMessage());

    }

    @DisplayName("제안 취소")
    @Test
    @WithAccount("yk")
    public void cancel() throws Exception {

        // Given
        Account account = accountRepository.findByNickname("yk");

        Set<String> tagSet = new HashSet<String>(Arrays.asList("algorithm", "kubernetes", "docker", "devops"));
        Set<String> zoneSet = new HashSet<String>(Arrays.asList("부산광역시/사하구", "부산광역시/북구", "부산광역시/금정구"));

        Mentee mentee = Mentee.createMentee(account,
                "멘티 설명",
                getZones(zoneSet),
                getTags(tagSet),
                true);
        menteeRepository.save(mentee);

        Account account2 = accountRepository.findByNickname("account2");
        Long mentorId = mentorRepository.findByAccountId(account2.getId()).getId();

        SuggestionRequest request = SuggestionRequest.builder()
                .message("message : yk -> account2")
                .build();
        Long suggestionId = suggestionService.suggest(account, mentorId, request);

        // When
        mockMvc.perform(post(String.format("/suggestion/%d/cancel", suggestionId)))
                .andDo(print())
                .andExpect(status().isOk());
        // Then
        List<Notification> notifications = notificationRepository.findByAccount(account2);
        assertEquals(2, notifications.size());
        List<String> titles = notifications.stream().map(notification -> notification.getTitle())
                .collect(Collectors.toList());
        assertTrue(titles.contains("제안이 취소되었습니다."));

        assertEquals(1, notifications.stream()
                .filter(notification -> notification.getMessage().equals(account.getName() + "님이 제안을 취소했습니다."))
                .collect(Collectors.toList()).size());
    }

    @DisplayName("제안 취소 불가")
    @Test
    @WithAccount("yk")
    public void cancel_notCorrectSender() throws Exception {

        // Given
        Account account = accountRepository.findByNickname("account1");
        Account account2 = accountRepository.findByNickname("account2");
        Long mentorId = mentorRepository.findByAccountId(account2.getId()).getId();

        SuggestionRequest request = SuggestionRequest.builder()
                .message("message : account1 -> account2")
                .build();
        Long suggestionId = suggestionService.suggest(account, mentorId, request);

        // When
        // Then
        mockMvc.perform(post(String.format("/suggestion/%d/cancel", suggestionId)))
                .andDo(print())
                .andExpect(status().is(400));
    }
}
