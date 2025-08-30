package org.nikita.core.service.register;

import lombok.RequiredArgsConstructor;
import org.nikita.core.domain.PersonEntity;
import org.nikita.core.domain.VerificationToken;
import org.nikita.core.repositories.VerificationTokenRepository;
import org.nikita.core.security.exceptions.VerificationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;

    public VerificationToken createVerificationToken(PersonEntity user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        return tokenRepository.save(verificationToken);
    }

    public PersonEntity validateVerificationToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new VerificationException("Verification token not found"));

        if (verificationToken.isExpired()) {
            tokenRepository.delete(verificationToken);
            throw new VerificationException("Verification token expired");
        }

        PersonEntity user = verificationToken.getUser();
        user.setEnabled(true);
        tokenRepository.delete(verificationToken);
        return user;
    }

}

