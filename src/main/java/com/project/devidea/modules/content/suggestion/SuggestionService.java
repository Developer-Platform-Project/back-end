package com.project.devidea.modules.content.suggestion;

import com.project.devidea.modules.account.Account;
import com.project.devidea.modules.content.mentoring.Mentee;
import com.project.devidea.modules.content.mentoring.MenteeRepository;
import com.project.devidea.modules.content.mentoring.Mentor;
import com.project.devidea.modules.content.mentoring.MentorRepository;
import com.project.devidea.modules.content.mentoring.exception.NotCorrectException;
import com.project.devidea.modules.content.mentoring.exception.NotFoundException;
import com.project.devidea.modules.content.suggestion.form.SuggestionRequest;
import com.project.devidea.modules.notification.Notification;
import com.project.devidea.modules.notification.NotificationRepository;
import com.project.devidea.modules.notification.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SuggestionService {

    static String NOTIFICATION_TITLE_NEW = "새로운 제안이 도착했습니다.";
    static String NOTIFICATION_TITLE_CANCEL = "제안이 취소되었습니다.";

    private final MentorRepository mentorRepository;
    private final MenteeRepository menteeRepository;
    private final SuggestionRepository suggestionRepository;
    private final NotificationRepository notificationRepository;

    public Long suggest(Account account, Long id, SuggestionRequest request) {

        Mentor findMentor = mentorRepository.findByAccountId(account.getId());
        Mentee findMentee = menteeRepository.findByAccountId(account.getId());
        Account receiver = null;
        if (findMentor != null && findMentee == null) {
            Mentee mentee = menteeRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("존재하지 않는 멘티입니다."));
            receiver = mentee.getAccount();
        } else if (findMentor == null && findMentee != null) {
            Mentor mentor = mentorRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("존재하지 않는 멘토입니다."));
            receiver = mentor.getAccount();
        } else {
            throw new NotFoundException();
        }

        Suggestion suggestion = Suggestion.createSuggestion(account, receiver, request.getMessage());
        // 알림 생성
        Notification notification = Notification.generateNotification(NOTIFICATION_TITLE_NEW,
                request.getMessage(), NotificationType.SUGGESTION, receiver);
        notificationRepository.save(notification);
        // TODO - 메일
        return suggestionRepository.save(suggestion).getId();
    }

    public void cancel(Account account, Long suggestionId) {

        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new NotFoundException());

        if (suggestion.getSender() != account) {
            throw new NotCorrectException("제안을 보낸 사람만 취소 가능합니다.");
        }
        // 알림 생성
        Notification notification = Notification.generateNotification(NOTIFICATION_TITLE_CANCEL,
                account.getName() + "님이 제안을 취소했습니다.", NotificationType.SUGGESTION, suggestion.getReceiver());
        notificationRepository.save(notification);
        // TODO - 메일
        suggestionRepository.delete(suggestion);
    }
}
