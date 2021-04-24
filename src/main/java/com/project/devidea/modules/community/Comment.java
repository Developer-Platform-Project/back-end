package com.project.devidea.modules.community;

import com.project.devidea.modules.account.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Community community;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Account account;
    private String content;
    private LocalDateTime createdDateTime;
    private LocalDateTime updatedDateTime;
}
