package com.project.devidea.modules.content.study;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.content.study.apply.StudyApply;
import com.project.devidea.modules.content.study.apply.StudyApplyForm;
import com.project.devidea.modules.content.study.exception.StudyNullException;
import com.project.devidea.modules.content.study.form.StudyDetailForm;
import com.project.devidea.modules.content.study.form.StudyListForm;
import com.project.devidea.modules.content.study.form.StudyMakingForm;
import com.project.devidea.modules.content.study.repository.StudyRepository;
import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.tag.TagRepository;
import com.project.devidea.modules.tagzone.zone.Zone;
import com.project.devidea.modules.tagzone.zone.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class StudyFactory {
    private final StudyRepository studyRepository;
    private final ModelMapper studyMapper;
    private final ZoneRepository zoneRepository;
    private final TagRepository tagRepository;
    public StudyApply getStudyApply(Study study, Account applicant, StudyApplyForm studyApplyForm) {
        StudyApply studyApply = StudyApply.builder()
                .study(study)
                .applicant(applicant)
                .answer(studyApplyForm.getAnswer())
                .etc(studyApplyForm.getEtc())
                .build();
        return studyApply;
    }
    @Transactional(readOnly = true)
    public StudyApply getStudyApply(Study study, Account account) {
        return new StudyApply().builder()
                .study(study)
                .applicant(account)
                .build();
    }
    @Transactional(readOnly = true)
    public StudyApply getStudyApply(Account applicant, @Valid StudyApplyForm studyApplyForm) {
        Study study = studyRepository.findById(studyApplyForm.getStudyId()).orElseThrow(StudyNullException::new);
        StudyApply studyApply=this.getStudyApply(study, applicant, studyApplyForm);
        return studyApply;
    }
    @Transactional(readOnly = true)
    public StudyApplyForm getStudyForm(Study study) {
        StudyApplyForm studyApplyForm = new StudyApplyForm()
                .builder()
                .studyId(study.getId())
                .study(study.getTitle())
                .answer(study.getQuestion())
                .applicant("")
                .build();
        return studyApplyForm;
    }
    @Transactional(readOnly = true)
    public StudyApplyForm getStudyForm(Long id) {
        Study study = studyRepository.findById(id).orElseThrow();
        return this.getStudyForm(study);
    }
    @Transactional(readOnly = true)
    public StudyMember getStudyMember(Study study, Account account, StudyRole role) {
        return StudyMember.builder()
                .study(study)
                .member(account)
                .JoinDate(LocalDateTime.now())
                .role(role)
                .build();
    }
    @Transactional(readOnly = true)
    public Study getStudy(StudyMakingForm studyMakingForm){
        Study study = studyMakingForm.toStudy();
        String[] locations = studyMakingForm.getLocation().split("/");
        Zone zone = zoneRepository.findByCityAndProvince(locations[0], locations[1]);
        Set<Tag> tagsSet = studyMakingForm.getTags().stream().map(tag -> {
            return tagRepository.findByFirstName(tag);
        }).collect(Collectors.toSet());
        study.setLocation(zone);
        study.setTags(tagsSet);
        study.setCounts(study.getCounts() + 1);
        return study;
    }
    @Transactional(readOnly = true)
    public StudyListForm getStudyListForm(Study study) {
        return studyMapper.map(study, StudyListForm.class);
    }
    @Transactional(readOnly = true)
    public StudyDetailForm getStudyDetailForm(Study study,Account admin){
        StudyDetailForm studyDetailForm = studyMapper.map(study, StudyDetailForm.class);
        studyDetailForm.setMembers(new HashSet<String>(Arrays.asList(admin.getNickname())));
        return studyDetailForm;
    }
    @Transactional(readOnly = true)
    public StudyDetailForm getStudyDetailForm(Long studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow();
        return studyMapper.map(study, StudyDetailForm.class);
    }


}
