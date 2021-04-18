package com.project.devidea.modules.content.study.notification;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.repository.InterestRepository;
import com.project.devidea.modules.content.study.Study;
import com.project.devidea.modules.content.study.StudyMember;
import com.project.devidea.modules.content.study.repository.StudyMemberRepository;
import com.project.devidea.modules.notification.Notification;
import com.project.devidea.modules.notification.NotificationRepository;
import com.project.devidea.modules.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class StudyNotificationImpl implements StudyNotification{
    private final NotificationRepository notificationRepository;
    private final InterestRepository interestRepository;
    private final StudyMemberRepository studyMemberRepository;
    static HashMap<StudyNoticationType,String> messages_releated;
    static HashMap<StudyNoticationType,String> messages_owner;
    @PostConstruct
    void init(){
        if(messages_releated==null){
            messages_releated=new HashMap<>();
            messages_owner=new HashMap<>();

            messages_releated.put(StudyNoticationType.JOIN,"스터디원이 들엉왔습니다.");
            messages_releated.put(StudyNoticationType.DELETE,"스터디가 삭제 됐습니다.");
            messages_releated.put(StudyNoticationType.APPLY,"스터디 신청이 있습니다.");
            messages_releated.put(StudyNoticationType.CREATED,"스터디가 생성됐습니다.");
            messages_releated.put(StudyNoticationType.CHANGED,"스터디가 변경됐습니다.");
            messages_releated.put(StudyNoticationType.LEAVED,"스터디원이 떠났습니다.");
            messages_releated.put(StudyNoticationType.REJECTED,"스터디 신청을 거절했습니다.");

            messages_owner.put(StudyNoticationType.JOIN,"스터디원이 됐습니다.");
            messages_owner.put(StudyNoticationType.DELETE,"스터디를 삭제 했습니다.");
            messages_owner.put(StudyNoticationType.APPLY,"스터디 신청을 성공적으로 하였습니다.");
            messages_owner.put(StudyNoticationType.CREATED,"스터디를 생성 하였습니다.");
            messages_owner.put(StudyNoticationType.CHANGED,"스터디를 설정을 변경 했습니다.");
            messages_owner.put(StudyNoticationType.LEAVED,"스터디를 떠났습니다.");
            messages_owner.put(StudyNoticationType.REJECTED,"스터디신청이 거절 됐습니다.");
        }
    }
    @Override
    public void sendRelated(Study study,StudyNoticationType type) {
        if (type == StudyNoticationType.CREATED) { //스터디가 생성 됐을때.. 관심등록한사람들한테 전부 보내기
            List<Account> accountList = interestRepository.findAccountByTagContains(study.getTags());
            List<Notification> notifications = accountList.stream()
                    .filter(account -> account.isReceiveStudyNotification() == true)
                    .map(account -> {
                        return Notification.generateNotification(study.getTitle(), messages_releated.get(type), NotificationType.STUDY, account);
                    }).collect(Collectors.toList());
            notificationRepository.saveAll(notifications);

        } else { //스터디원한테 보낼때
            List<StudyMember> studyMembers = studyMemberRepository.findByStudy_Id(study.getId());
            List<Notification> notifications = studyMembers.stream().map(studyMember -> {
                return Notification.generateNotification(study.getTitle(), messages_releated.get(type), NotificationType.STUDY, studyMember.getMember());
            }).collect(Collectors.toList());
            notificationRepository.saveAll(notifications);
        }
    }
    @Override
    public void sendOwn(Study study, Account account,StudyNoticationType type) {
        notificationRepository.save(Notification.generateNotification(study.getTitle(), messages_owner.get(type), NotificationType.STUDY,account));
    }

    @Override
    public void sendAll(Study study, Account account,StudyNoticationType type) {
        sendOwn(study,account,type);
        sendRelated(study,type);
    }
}
