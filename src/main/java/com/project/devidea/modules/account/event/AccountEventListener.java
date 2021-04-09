package com.project.devidea.modules.account.event;

import com.project.devidea.infra.config.AppProperties;
import com.project.devidea.infra.mail.EmailMessage;
import com.project.devidea.infra.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Async
@Component
@Transactional
@RequiredArgsConstructor
public class AccountEventListener {

    private final AppProperties appProperties;
    private final EmailService emailService;

    @EventListener
    public void sendSignUpAuthenticationEmail(SendEmailToken sendEmailToken) {

        String msg = "해당 주소로 접속하시면, 회원가입이 완료됩니다.\n"
                + appProperties.getHost() + "/check-email-token?email=" + sendEmailToken.getTo()
                + "&token=" + sendEmailToken.getToken();

        EmailMessage message = EmailMessage.builder()
                .subject("[DevIdea] 회원가입 인증 메일입니다.")
                .to(sendEmailToken.getTo())
                .message(msg)
                .build();
        emailService.sendEmail(message);
    }
}
