package org.nikita.core.service.register;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.nikita.core.domain.PersonEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendVerificationHtmlEmail(PersonEntity user, String token) throws MessagingException {
        String subject = "Подтверждение регистрации";
        String confirmationUrl = "http://localhost:8080/api/auth/verify?token=" + token;
        String htmlMsg = String.format(
                "<p>Здравствуйте, %s!</p>" +
                        "<p>Спасибо за регистрацию на нашем сайте.</p>" +
                        "<p>Пожалуйста, <a href='%s'>нажмите здесь</a>, чтобы подтвердить вашу регистрацию.</p>" +
                        "<p>Если вы не регистрировались на нашем сайте, просто проигнорируйте это письмо.</p>" +
                        "<br/><p>С уважением,<br/>Команда Travel Insurance</p>",
                user.getFirstName(), confirmationUrl);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(htmlMsg, true); // true = HTML
        mailSender.send(message);
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(text);

        mailSender.send(email);
    }
}

