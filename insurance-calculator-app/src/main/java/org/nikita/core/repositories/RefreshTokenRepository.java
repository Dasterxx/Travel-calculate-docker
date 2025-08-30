package org.nikita.core.repositories;

import org.nikita.core.domain.PersonEntity;
import org.nikita.core.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(PersonEntity user);
    @Transactional
    void deleteByUserId(Long userId);
}

