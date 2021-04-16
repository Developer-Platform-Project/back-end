package com.project.devidea.modules.content.study.controller;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.modules.content.study.exception.AlreadyApplyException;
import com.project.devidea.modules.content.study.service.StudyServiceImpl;
import com.project.devidea.modules.content.study.apply.StudyApplyForm;
import com.project.devidea.modules.content.study.form.*;
import com.project.devidea.modules.content.study.repository.StudyMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RestController
@RequiredArgsConstructor
public class StudyBasicController {
    private final StudyServiceImpl studyService;
    private final StudyMemberRepository studyMemberRepository;

    @GetMapping("/study")
    public ResponseEntity<?> 조회(@Valid StudySearchForm searchForm) {
        return new ResponseEntity<>(studyService.searchByCondition(searchForm), HttpStatus.OK);
    }

    @PostMapping("/study")
    public ResponseEntity<?> 등록(@AuthenticationPrincipal LoginUser account, @RequestBody @Valid StudyMakingForm studyMakingForm) {
//        if (account.getNickName() == null) return new ResponseEntity<>("로그인 해주십쇼.", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(studyService.makingStudy(account.getAccount(), studyMakingForm), HttpStatus.OK);
    }

    @PostMapping("/study/mystudy")
    public ResponseEntity<?> 내스터디(@AuthenticationPrincipal LoginUser account) {
        return new ResponseEntity<>(studyService.myStudy(account.getAccount()), HttpStatus.OK);
    }

    @GetMapping("/study/{id}")
    public ResponseEntity<?> 스터디_자세한정보(@PathVariable Long id) {
        return new ResponseEntity<>(studyService.getDetailStudy(id), HttpStatus.OK);
    }


    @PostMapping("/study/{id}/leave")
    public ResponseEntity<?> 스터디에서_나가기(@AuthenticationPrincipal LoginUser account, @PathVariable Long id) {
        return new ResponseEntity<>(studyService.leaveStudy(account.getAccount(), id), HttpStatus.OK);
    }

    @GetMapping("/study/{id}/applyform")
    public ResponseEntity<?> 가입_신청폼받기(@PathVariable Long id) {
        return new ResponseEntity<>(studyService.makeStudyForm(id), HttpStatus.OK);
    }

    @PostMapping("/study/{id}/apply")
    public ResponseEntity<?> 가입_신청하기(@AuthenticationPrincipal LoginUser account,@PathVariable Long id,
                                     @RequestBody StudyApplyForm studyApplyForm) throws  AlreadyApplyException {
        return new ResponseEntity<>(studyService.applyStudy(account.getAccount(),studyApplyForm), HttpStatus.OK);
    }
    @GetMapping("/study/{id}/applylist")
    public ResponseEntity<?> 내_가입_신청리스트(@AuthenticationPrincipal LoginUser account) {
        return new ResponseEntity<>(studyService.myApplyList(account.getAccount()), HttpStatus.OK);
    }
}
