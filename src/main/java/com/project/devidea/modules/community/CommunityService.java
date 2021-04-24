package com.project.devidea.modules.community;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.community.form.RequestCommunity;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityService {

    private final CommunityRepository communityRepository;

    @Transactional
    public Community createCommunity(Account writer, RequestCommunity requestCommunity) {
        Community createdCommunity = Community.builder()
                .title(requestCommunity.getTitle())
                .content(requestCommunity.getContent())
                .writer(writer)
                .communityType(Enum.valueOf(CommunityType.class,requestCommunity.getCommunityType()))
                .createdDateTime(LocalDateTime.now())
                .updatedDateTime(LocalDateTime.now())
                .build();
        communityRepository.save(createdCommunity);
        return createdCommunity;
    }
}
