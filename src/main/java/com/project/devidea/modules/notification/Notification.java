package com.project.devidea.modules.notification;


import com.project.devidea.modules.account.domains.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;
    private boolean checked;

    private LocalDateTime createdDateTime;
    private LocalDateTime checkedDateTime;

    public void markAsRead() {
        if (!this.checked) {
            this.checked = true;
            checkedDateTime = LocalDateTime.now();
        }
    }

    @Transient
    public static Notification generateNotification(String title, String message, NotificationType notificationType,
                                                    Account account) {
    return new Notification().builder()
            .title(title)
            .message(message)
            .notificationType(notificationType)
            .account(account)
            .createdDateTime(LocalDateTime.now())
            .checked(false)
            .build();
    }

    @Builder
    public Notification(Long id, String title, String message, NotificationType notificationType, String link, Account account, boolean checked, LocalDateTime createdDateTime, LocalDateTime checkedDateTime) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
        this.link = link;
        this.account = account;
        this.checked = checked;
        this.createdDateTime = createdDateTime;
        this.checkedDateTime = checkedDateTime;
    }
}
