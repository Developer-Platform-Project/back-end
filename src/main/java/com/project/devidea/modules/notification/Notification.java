package com.project.devidea.modules.notification;


import com.project.devidea.modules.account.domains.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
