package org.nikita.core.repositories;

import org.nikita.core.domain.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    Optional<CountryEntity> findByCode(String code);
    List<CountryEntity> findByCodeIn(List<String> codes);
}

