package com.project.devidea.modules.content.suggestion;

import com.project.devidea.modules.account.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suggestion {

    @Id @GeneratedValue
    @Column(name = "suggestion_id")
    private Long id;

    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender")
    private Account sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver")
    private Account receiver;

//    private String subject;
    private String message;

    public static Suggestion createSuggestion(Account sender, Account receiver, String message) {

        Suggestion suggestion = new Suggestion();
        suggestion.setSender(sender);
        suggestion.setReceiver(receiver);
        suggestion.setMessage(message);

        suggestion.setDateTime(LocalDateTime.now());
        return suggestion;
    }
}
