package org.nikita.core.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.nikita.api.dto.CountryDto;
import org.nikita.core.domain.CountryEntity;
import org.nikita.core.repositories.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    public List<CountryDto> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public CountryDto getCountryByCode(String code) {
        return countryRepository.findByCode(code)
                .map(this::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Country not found"));
    }

    public List<CountryDto> getCountriesByCodes(List<String> codes) {
        return countryRepository.findByCodeIn(codes).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<CountryEntity> getCountryEntitiesByCodes(List<String> codes) {
        return countryRepository.findByCodeIn(codes);
    }

    private CountryDto toDto(CountryEntity entity) {
        return new CountryDto(entity.getCode(), entity.getName());
    }
}


