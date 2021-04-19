package com.project.devidea.modules.community;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.account.AccountDummy;
import com.project.devidea.modules.notification.Notification;
import com.project.devidea.modules.notification.NotificationType;

import java.util.ArrayList;
import java.util.List;

public class CommunityDummy {

    public static List<Community> getCommunities(Account account) {
        List<Community> communities = new ArrayList<>();

        Community communityA = Community.builder()
                .title("커뮤니티제목1")
                .content("커뮤니티내용1")
                .writer(account)
                .communityType(CommunityType.FREE).build();

        Community communityB = Community.builder()
                .title("커뮤니티제목2")
                .content("커뮤니티내용2")
                .writer(account)
                .communityType(CommunityType.FREE).build();

        Community communityC = Community.builder()
                .title("커뮤니티제목3")
                .content("커뮤니티내용3")
                .writer(account)
                .communityType(CommunityType.FREE).build();

        communities.add(communityA);
        communities.add(communityB);
        communities.add(communityC);

        return communities;
    }
}
