package com.project.devidea.modules.content.study.aop;

import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.content.study.Study;
import com.project.devidea.modules.content.study.StudyFactory;
import com.project.devidea.modules.content.study.apply.StudyApplyForm;
import com.project.devidea.modules.content.study.exception.StudyNullException;
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
public class StudyNotificationAspect {
    private final StudyFactory studyFactory;
    private final StudyRepository studyRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyNotification studyNotification;

    //스터디 성공적으로 만들었을떄
    @AfterReturning(value = "execution(* com.project.devidea.modules.content.study.service.StudyServiceImpl.makingStudy(..)) && args(admin,..)", returning = "study")
    public void makingStudySucess(JoinPoint joinPoint, Study study, Account admin) throws RuntimeException {
        studyNotification.sendAll(study, admin, StudyNoticationType.CREATED);
    }

    //인원 추가 했을때
    @AfterReturning(value = "execution(* com.project.devidea.modules.content.study.service.StudyServiceImpl.addMember(..)) && args(applicant,..)", returning = "study")
    public void addStudyMemberSucess(JoinPoint joinPoint, Study study, Account applicant) throws RuntimeException {
        studyNotification.sendAll(study, applicant, StudyNoticationType.JOIN);
    }

    //스터디 삭제하기전에
    @Before(value = "execution(* com.project.devidea.modules.content.study.service.StudyServiceImpl.deleteStudy(..)) && args(id,account)")
    public void BeforeDeleteStudySuccess(JoinPoint joinPoint, Long id, Account account) throws RuntimeException {
        studyNotification.sendAll(studyRepository.findById(id).orElseThrow(() -> new StudyNullException()), account, StudyNoticationType.DELETE);
    }

    //스터디 성공적으로 떠낫을때
    @AfterReturning(value = "execution(* com.project.devidea.modules.content.study.service.StudyServiceImpl.leaveStudy(..)) && args(id,account)")
    public void LeaveStudySuccess(JoinPoint joinPoint, Long id, Account account) throws RuntimeException {
        studyNotification.sendAll(studyRepository.findById(id).orElseThrow(() -> new StudyNullException()), account, StudyNoticationType.LEAVED);
    }
    //스터디 설정변경했을때
    @AfterReturning(value = "execution(* com.project.devidea.modules.content.study.service.StudyServiceImpl.Update*(..)) && args(id)")
    public void UpdateStudySuccess(JoinPoint joinPoint, Long id) throws RuntimeException {
        studyNotification.sendRelated(studyRepository.findById(id).orElseThrow(() -> new StudyNullException()), StudyNoticationType.CHANGED);
    }
    //스터디 지원시
    @AfterReturning(value = "execution(* com.project.devidea.modules.content.study.service.StudyServiceImpl.applyStudy(..)) && args(applicant,studyApplyForm)")
    public void StudyApplySuccess(JoinPoint joinPoint, StudyApplyForm studyApplyForm, Account applicant) throws RuntimeException {
        studyNotification.sendRelated(studyRepository.findById(studyApplyForm.getStudyId()).orElseThrow(StudyNullException::new), StudyNoticationType.APPLY);
    }

    //스터디 거절시
    @AfterReturning(value = "execution(* com.project.devidea.modules.content.study.service.StudyServiceImpl.rejected(..)) && args(study,applicant)")
    public void StudyApplyRejected(JoinPoint joinPoint, Study study, Account applicant) throws RuntimeException {
        studyNotification.sendOwn(study,applicant, StudyNoticationType.REJECTED);
    }
}
