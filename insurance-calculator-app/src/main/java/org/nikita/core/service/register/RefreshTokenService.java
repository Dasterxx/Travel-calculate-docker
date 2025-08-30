package org.nikita.core.service.register;

import lombok.RequiredArgsConstructor;
import org.nikita.core.domain.PersonEntity;
import org.nikita.core.domain.RefreshToken;
import org.nikita.core.repositories.PersonRepository;
import org.nikita.core.repositories.RefreshTokenRepository;
import org.nikita.core.security.exceptions.RefreshTokenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final PersonRepository userRepository;

    private final long refreshTokenDurationMs = 7 * 24 * 60 * 60 * 1000; // 7 дней

    public RefreshToken createRefreshToken(PersonEntity user) {
        refreshTokenRepository.deleteByUser(user); // Удалить старые

        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(user, token, new Date(System.currentTimeMillis() + refreshTokenDurationMs));
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByTokenOrThrow(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Refresh token not found"));
    }

    public void validateRefreshTokenOrThrow(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException("Refresh token expired");
        }
    }


    public void deleteByUser(PersonEntity user) {
        refreshTokenRepository.deleteByUser(user);
    }
}

