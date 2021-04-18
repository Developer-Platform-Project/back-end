package com.project.devidea.modules.content.study.service;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.repository.AccountRepository;
import com.project.devidea.modules.content.study.Study;
import com.project.devidea.modules.content.study.StudyFactory;
import com.project.devidea.modules.content.study.StudyMember;
import com.project.devidea.modules.content.study.StudyRole;
import com.project.devidea.modules.content.study.aop.AlreadyExistError;
import com.project.devidea.modules.content.study.apply.StudyApply;
import com.project.devidea.modules.content.study.apply.StudyApplyForm;
import com.project.devidea.modules.content.study.apply.StudyApplyListForm;
import com.project.devidea.modules.content.study.apply.StudyApplyRepository;
import com.project.devidea.modules.content.study.exception.AlreadyStudyExistsException;
import com.project.devidea.modules.content.study.exception.AlreadyStudyExistsException;
import com.project.devidea.modules.content.study.exception.StudyNullException;
import com.project.devidea.modules.content.study.form.*;
import com.project.devidea.modules.content.study.repository.StudyMemberRepository;
import com.project.devidea.modules.content.study.repository.StudyRepository;
import com.project.devidea.modules.notification.Notification;
import com.project.devidea.modules.notification.NotificationRepository;
import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.tag.TagRepository;
import com.project.devidea.modules.tagzone.zone.Zone;
import com.project.devidea.modules.tagzone.zone.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Configuration
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {
    private final StudyFactory studyFactory;
    private final ModelMapper studyMapper;
    private final StudyRepository studyRepository;
    private final AccountRepository accountRepository;
    private final StudyApplyRepository studyApplyRepository;
    private final StudyMemberRepository studyMemberRepository;

    public List<StudyListForm> searchByCondition(@Valid StudySearchForm studySearchForm) {
        List<Study> studyList = studyRepository.findByCondition(studySearchForm);
        return studyList.stream().map(study -> {
            return studyMapper.map(study, StudyListForm.class);
        }).collect(Collectors.toList());
    }


    public StudyDetailForm getDetailStudy(Long id) {
        return studyFactory.getStudyDetailForm(id);
    }

    public String applyStudy(Account applicant,  StudyApplyForm studyApplyForm) throws AlreadyStudyExistsException{
        studyApplyRepository.saveAndFlush(studyFactory.getStudyApply(applicant,studyApplyForm));
        return "Yes";//완료 메시지 미완성
    }

    public String decideJoin(Long studyApplyId, Boolean accept) {
        StudyApply studyApply = studyApplyRepository.findById(studyApplyId).orElseThrow();
        Account applicant = studyApply.getApplicant();
        Study study = studyApply.getStudy();
        if (study.getCounts() == study.getMaxCount()) return "인원이 꽉찼습니다.";
        studyApply.setAccpted(accept);
        if (accept) {
            return addMember(applicant, study, StudyRole.회원);
        } else return rejected(study,applicant);
    }

    public String addMember(Account applicant, Study study, StudyRole role) throws AlreadyStudyExistsException{
        studyMemberRepository.saveAndFlush(studyFactory.getStudyMember(study,applicant,role));
        return "성공적으로 저장하였습니다.";
    }
    public String rejected(Study study,Account applicant){
        return "스터디 요청을 거절하였습니다.";
    }

    @Override
    public List<StudyApplyForm> getApplyFormList(Long id) { //해당 스터디 가입신청 리스트 보기
        return studyApplyRepository.findById(id).stream()
                .map(studyApply -> {
                    return studyMapper.map(studyApply, StudyApplyForm.class);
                }).collect(Collectors.toList());
    }

    @Override
    public List<StudyListForm> getMyStudy(Account account) {
        List<StudyMember> studyList = studyMemberRepository.findByMember_Id(account.getId());
        return studyList.stream().map(study -> {
            return studyMapper.map(study.getStudy(), StudyListForm.class);
        }).collect(Collectors.toList());
    }


    @Override
    public List<StudyApplyForm> getMyApplyList(Account account) {
        return null;
    }

    public String deleteStudy(Account account,Long id) { //해당 스터디 가입신청 리스트 보기
        Study study = studyRepository.findById(id).orElseThrow();
        studyRepository.delete(study);
        return "성공적으로 삭제하였습니다.";
    }

    public String leaveStudy(Account account, Long study_id) {
        studyMemberRepository.deleteByStudy_IdAndMember_Id(study_id, account.getId());
        studyRepository.LeaveStudy(study_id);
        return "스터디를 떠났습니다.";
    }
    
    public List<StudyApplyListForm> getApplyList(Long id) {
        return studyApplyRepository.findByStudy_Id(id).stream().map(
                studyApply -> {
                    return studyMapper.map(studyApply, StudyApplyListForm.class);
                }
        ).collect(Collectors.toList());
    }

    public StudyApplyForm getApplyDetail(Long id) {
        StudyApply studyApply = studyApplyRepository.findById(id).get();
        return studyMapper.map(studyApply, StudyApplyForm.class);
    }

    public OpenRecruitForm getOpenRecruitForm(Long id) {
        Study study = studyRepository.findById(id).orElseThrow();
        return new OpenRecruitForm(study.isOpen(), study.isRecruiting());
    }

    public TagZoneForm getTagandZone(Long id) {
        Study study = studyRepository.findById(id).orElseThrow();
        return new TagZoneForm(study.getTags(), study.getLocation());
    }

    @Override
    public StudyDetailForm makingStudy(Account admin, @Valid StudyMakingForm studyMakingForm) throws AlreadyStudyExistsException {
        Study study = studyFactory.getStudy(studyMakingForm);
        studyRepository.save(study);
        studyMemberRepository.saveAndFlush(studyFactory.getStudyMember(study, admin, StudyRole.팀장));
        return studyFactory.getStudyDetailForm(study,admin);
    }

    public String UpdateOpenRecruiting(Long id, OpenRecruitForm openRecruitForm) {
        Study study = studyRepository.findById(id).orElseThrow();
        study.setOpenAndRecruiting(openRecruitForm.isOpen(), openRecruitForm.isRecruiting());
        return "success";
    }

    public String UpdateTagAndZone(Long id, TagZoneForm tagZoneForm) {
        Study study = studyRepository.findById(id).orElseThrow();
        return "success";
    }

    public String setEmpower(Long study_id, EmpowerForm empowerForm) {
        studyMemberRepository.updateRole(study_id, accountRepository.findByNickname(empowerForm.getNickName()).getId(), empowerForm.getRole());
        return "성공적으로 권한을 부여했습니다.";
    }

    public List<StudyApplyForm> myApplyList(Account account) {
        List<StudyApply> studyApplies = studyApplyRepository.findByApplicant(account);
        return studyApplies.stream().map(studyApply -> {
                    return studyMapper.map(studyApply, StudyApplyForm.class);
                }
        ).collect(Collectors.toList());
    }

    public StudyApplyForm getStudyApplyForm(Long studyId) {
        return studyFactory.getStudyForm( studyRepository.findById(studyId).orElseThrow(StudyNullException::new));
    }
}
