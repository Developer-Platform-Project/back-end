package com.project.devidea.modules.community;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.modules.community.form.RequestCommunity;
import com.project.devidea.modules.content.study.form.StudyMakingForm;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("")
    public ResponseEntity createCommunity(@AuthenticationPrincipal LoginUser account, @RequestBody @Valid RequestCommunity requestCommunity) {
        Community createdCommunity = communityService.createCommunity(account.getAccount(), requestCommunity);
        return new ResponseEntity(createdCommunity,HttpStatus.OK);
    }
}
