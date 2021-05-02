package com.project.devidea.modules.notification;

import com.project.devidea.modules.account.domains.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void markAsRead(List<Notification> notifications) {
        notifications.forEach(it -> it.markAsRead());
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public List<Notification> createNotification(NotificationType type, Long id, List<Account> accountList, String title, String message) {

        String link = (id != 0) ?  "/" + type + "/" + id : "";

        List<Notification> notifications = accountList.stream()
                .map(account -> {
                    return Notification.builder()
                            .notificationType(type)
                            .account(account)
                            .title(title)
                            .message(message)
                            .checked(false)
                            .link(link)
                            .createdDateTime(LocalDateTime.now()).build();
                }).collect(Collectors.toList());
        notificationRepository.saveAll(notifications);

       return notifications;
    }
}
