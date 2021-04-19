package com.project.devidea.infra.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        SimpleMailMessage sendMessage = new SimpleMailMessage();
        sendMessage.setSubject(emailMessage.getSubject());
        sendMessage.setTo(emailMessage.getTo());
        sendMessage.setText(emailMessage.getMessage());

        log.info("sent email: {}", emailMessage.getMessage());
        mailSender.send(sendMessage);
    }
}
