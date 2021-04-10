package com.project.devidea.modules.content.study.aop;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.repository.InterestRepository;
import com.project.devidea.modules.content.study.Study;
import com.project.devidea.modules.content.study.apply.StudyApplyRepository;
import com.project.devidea.modules.content.study.form.StudyDetailForm;
import com.project.devidea.modules.content.study.notification.*;
import com.project.devidea.modules.content.study.repository.StudyMemberRepository;
import com.project.devidea.modules.content.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class StudyNotificationAop {
    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyNotification studyNotification;

    //스터디 성공적으로 만들었을떄
    @AfterReturning(value = "execution(* com.project.devidea.modules.content.study.service.StudyServiceImpl.makingStudyEntity()) && args(admin,..)", returning = "study")
    public void makingStudySucess(JoinPoint joinPoint, Study study, Account admin) throws RuntimeException {
        studyNotification.sendAll(study, admin, StudyNoticationType.CREATED);
    }

    //인원 추가 했을때
    @AfterReturning(value = "execution(* com.project.devidea.modules.content.study.service.StudyServiceImpl.addMember()) && args(applicant,..)", returning = "study")
    public void addStudyMemberSucess(JoinPoint joinPoint, Study study, Account applicant) throws RuntimeException {
        studyNotification.sendAll(study, applicant, StudyNoticationType.JOIN);
    }

    //스터디 삭제하기전에
    @Before(value = "execution(* com.project.devidea.modules.content.study.service.StudyServiceImpl.deleteStudy()) && args(id,account)")
    public void BeforeDeleteStudySuccess(JoinPoint joinPoint, Long id, Account account) throws RuntimeException {
        studyNotification.sendAll(studyRepository.findById(id).orElseThrow(), account, StudyNoticationType.DELETE);
    }

    //스터디 성공적으로 떠낫을때
    @AfterReturning(value = "execution(* com.project.devidea.modules.content.study.service.StudyServiceImpl.leaveStudy()) && args(id,account)")
    public void LeaveStudySuccess(JoinPoint joinPoint, Long id, Account account) throws RuntimeException {
        studyNotification.sendAll(studyRepository.findById(id).orElseThrow(), account, StudyNoticationType.LEAVED);
    }
    //스터디 설정변경했을때
    @AfterReturning(value = "execution(* com.project.devidea.modules.content.study.service.StudyServiceImpl.Update*()) && args(id)")
    public void UpdateStudySuccess(JoinPoint joinPoint, Long id) throws RuntimeException {
        studyNotification.sendRelated(studyRepository.findById(id).orElseThrow(), StudyNoticationType.CHANGED);
    }
}
