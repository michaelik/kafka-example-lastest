package com.kafka.service.impl;

import com.kafka.dtos.EmailDTO;
import com.kafka.service.EmailMessageSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailMessageSenderServiceImpl implements EmailMessageSenderService {

    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(EmailDTO request) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true
            );
            helper.setFrom("mailtrap@demomailtrap.com");
            helper.setTo(request.to().toArray(new String[0]));
            helper.setSubject(request.subject());
            helper.setText(request.body(), true);
            mailSender.send(message);

            log.info("Email sent successfully to {}", request.to());
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", request.to().toString(), e);
            throw new RuntimeException(e);
        }
    }

    private String[] convertListToArray(List<String> recipients) {
        return recipients.stream()
                .map(String::trim)
                .toArray(String[]::new);
    }
}
