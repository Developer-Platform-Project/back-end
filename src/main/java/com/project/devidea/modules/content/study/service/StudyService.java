package com.project.devidea.modules.content.study.service;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.content.study.Study;
import com.project.devidea.modules.content.study.StudyMember;
import com.project.devidea.modules.content.study.StudyRole;
import com.project.devidea.modules.content.study.apply.StudyApply;
import com.project.devidea.modules.content.study.apply.StudyApplyForm;
import com.project.devidea.modules.content.study.apply.StudyApplyListForm;
import com.project.devidea.modules.content.study.exception.AlreadyStudyExistsException;
import com.project.devidea.modules.content.study.form.*;
import com.project.devidea.modules.notification.Notification;
import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.zone.Zone;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface StudyService {
    //스터디관련 검색 및 조회
    public List<StudyListForm> searchByCondition(@Valid StudySearchForm studySearchForm);
    public List<StudyApplyForm> getApplyFormList(Long id);
    public List<StudyListForm> getMyStudy(Account account);
    public List<StudyApplyForm> getMyApplyList(Account account);
    public List<StudyApplyListForm> getApplyList(Long id); //studyid
    public StudyDetailForm getDetailStudy(Long id);
    public StudyApplyForm getApplyDetail(Long id);
    public OpenRecruitForm getOpenRecruitForm(Long id);
    public TagZoneForm getTagandZone(Long id);
    public StudyApplyForm getStudyApplyForm(Long id);
    //스터디관련 엔티티 생성 작업들
    public StudyDetailForm makingStudy(Account admin, @Valid StudyMakingForm studyMakingForm);
    public String applyStudy(Account applicant, @Valid StudyApplyForm studyApplyForm) throws AlreadyStudyExistsException;
    public String addMember(Account applicant, Study study, StudyRole role);

    //스터디 관련 삭제 작업들
    public String deleteStudy(Account account,Long id);
    public String leaveStudy(Account account, Long study_id);

    //스터디 관련 수정 작업들
    public String UpdateOpenRecruiting(Long id, OpenRecruitForm openRecruitForm);
    public String UpdateTagAndZone(Long id, TagZoneForm tagZoneForm);
    public String decideJoin(Long id, Boolean accept);
    public String setEmpower(Long study_id, EmpowerForm empowerForm);
}
