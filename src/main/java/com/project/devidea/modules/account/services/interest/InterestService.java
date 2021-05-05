package com.project.devidea.modules.account.services.interest;

import com.project.devidea.modules.account.domains.Account;
import com.project.devidea.modules.account.domains.Interest;
import com.project.devidea.modules.tagzone.tag.Tag;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface InterestService {

    Set<Interest> getInterests(List<String> interests, Account account);

    default Set<Interest> tagsToInterests(Account account, List<Tag> tags){
        Set<Interest> interests = new HashSet<>();
        tags.forEach(tag -> {
            interests.add(Interest.builder().tag(tag).account(account).build());
        });
        return interests;
    }

    void saveAll(Set<Interest> interests);
}
