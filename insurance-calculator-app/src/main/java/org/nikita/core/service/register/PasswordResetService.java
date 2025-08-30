package org.nikita.core.service.register;

import lombok.RequiredArgsConstructor;
import org.nikita.core.domain.PasswordResetToken;
import org.nikita.core.domain.PersonEntity;
import org.nikita.core.repositories.PasswordResetTokenRepository;
import org.nikita.core.repositories.PersonRepository;
import org.nikita.core.security.exceptions.PasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final PasswordResetTokenRepository resetTokenRepository;
    private final PersonRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public void createPasswordResetToken(String email) {
        PersonEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new PasswordException("User with this email not found"));
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        resetTokenRepository.save(resetToken);

        String resetUrl = "http://localhost:8080/api/auth/reset-password?token=" + token;
        String message = "Для восстановления пароля перейдите по ссылке: " + resetUrl;
        emailService.sendSimpleMessage(user.getEmail(), "Восстановление пароля", message);
    }

    public boolean resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new PasswordException("Invalid or expired token"));


        if (resetToken.isExpired()) {
            resetTokenRepository.delete(resetToken);
            throw new PasswordException("Token expired");
        }

        PersonEntity user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetTokenRepository.delete(resetToken);
        return true;
    }
}

