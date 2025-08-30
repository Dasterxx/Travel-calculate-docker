package org.nikita.core.repositories;

import org.nikita.core.domain.CountryDefaultDayRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryDefaultDayRateRepository extends JpaRepository<CountryDefaultDayRate, Integer> {
    boolean existsByCountryName(String countryName);
    Optional<CountryDefaultDayRate> findByCountryName(String countryName);
}

