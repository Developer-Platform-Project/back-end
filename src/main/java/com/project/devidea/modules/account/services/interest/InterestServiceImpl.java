package com.project.devidea.modules.account.services.interest;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.Interest;
import com.project.devidea.modules.account.repository.InterestRepository;
import com.project.devidea.modules.tagzone.tag.Tag;
import com.project.devidea.modules.tagzone.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class InterestServiceImpl implements InterestService {

    private final TagService tagService;
    private final InterestRepository interestRepository;

    @Override
    public Set<Interest> getInterests(List<String> interests, Account account) {
        List<Tag> tags = tagService.findInterests(interests);
        return tagsToInterests(account, tags);
    }

    @Override
    public void saveAll(Set<Interest> interests) {
        interestRepository.saveAll(interests);
    }
}
