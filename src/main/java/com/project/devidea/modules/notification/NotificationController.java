package com.project.devidea.modules.notification;

import com.project.devidea.infra.config.security.LoginUser;
import com.project.devidea.infra.error.exception.EntityNotFoundException;
import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.content.techNews.TechSite;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/new")
    @ApiOperation("읽지 않은 알람들 조회")
    public ResponseEntity<?> getNewNotifications(@AuthenticationPrincipal LoginUser loginUser) {
        Account account = loginUser.getAccount();
        List<Notification> notifications = notificationRepository.findByAccountAndCheckedOrderByCreatedDateTimeDesc(account, false);
        int countOldNotifications = notificationRepository.countByAccountAndChecked(account, true);
        return new ResponseEntity<>(new ResponseNotifications(notifications.size(), countOldNotifications, notifications), HttpStatus.OK);
    }

    @GetMapping("/old")
    @ApiOperation("읽은 알람들 조회")
    public ResponseEntity<?> getOldNotifications(@AuthenticationPrincipal LoginUser loginUser) {
        Account account = loginUser.getAccount();
        List<Notification> notifications = notificationRepository.findByAccountAndCheckedOrderByCreatedDateTimeDesc(account, true);
        int countNewNotifications = notificationRepository.countByAccountAndChecked(account, false);
        return new ResponseEntity<>(new ResponseNotifications(notifications.size(), countNewNotifications, notifications), HttpStatus.OK);
    }

    @PostMapping("/delete")
    @ApiOperation("읽은 알람 전체 삭제")
    public ResponseEntity<?> deleteAllOldNotifications(@AuthenticationPrincipal LoginUser loginUser) {
        notificationRepository.deleteByAccountAndChecked(loginUser.getAccount(), true);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/delete")
    @ApiOperation("특정 알람 삭제")
    public ResponseEntity<?> deleteNotification(@PathVariable("id") Long id) {
        Optional<Notification> findNotification = notificationRepository.findById(id);
        if(!findNotification.isPresent())
            throw new EntityNotFoundException("notification");

        notificationRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/entityErrorTest")
    public ResponseEntity<?> entityErrorTest(Long id) {
        Optional<Notification> findNotification = notificationRepository.findById(id);
        if(!findNotification.isPresent())
            throw new EntityNotFoundException("notification");

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ResponseNotifications<T> {
        private int count;
        private int otherCount;
        private T notifications;
    }
}
