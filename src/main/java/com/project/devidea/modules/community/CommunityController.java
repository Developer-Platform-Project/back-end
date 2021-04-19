package com.project.devidea.modules.community;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.infra.error.exception.EntityNotFoundException;
import com.project.devidea.modules.community.form.RequestCommunity;
import com.project.devidea.modules.content.study.form.StudyMakingForm;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {

    private final CommunityService communityService;
    private final CommunityRepository communityRepository;

    @PostMapping("")
    public ResponseEntity createCommunity(@AuthenticationPrincipal LoginUser account, @RequestBody @Valid RequestCommunity requestCommunity) {
        Community createdCommunity = communityService.createCommunity(account.getAccount(), requestCommunity);
        return new ResponseEntity(createdCommunity,HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity getCommunities() {
        return new ResponseEntity(communityRepository.findAll(),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCommunityDetail(@PathVariable Long id) {
        return new ResponseEntity(communityRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("community")),HttpStatus.OK);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity deleteCommunity(@AuthenticationPrincipal LoginUser account, @PathVariable Long id) {
        Community findCommunity = communityRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("community"));
        communityRepository.delete(findCommunity);
        return new ResponseEntity(HttpStatus.OK);
    }
}
