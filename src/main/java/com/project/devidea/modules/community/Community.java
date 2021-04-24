package com.project.devidea.modules.community;

import com.project.devidea.modules.account.Account;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Account writer;
    private String title;
    private String content;

    @Enumerated
    private CommunityType communityType;

    @OneToMany
    private List<Comment> comments;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;

    @Builder
    public Community(Long id, Account writer, String title, String content, CommunityType communityType, List<Comment> comments, LocalDateTime createdDateTime, LocalDateTime updatedDateTime) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.communityType = communityType;
        this.comments = comments;
        this.createdDateTime = createdDateTime;
        this.updatedDateTime = updatedDateTime;
    }
}
