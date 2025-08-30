package org.nikita.core.service.agreement;

import lombok.RequiredArgsConstructor;
import org.nikita.core.domain.CountryEntity;
import org.nikita.core.repositories.CountryRepository;
import org.nikita.core.service.CountryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
class AgreementCountryService {

    private final CountryService countryService;
    private final CountryRepository countryRepository;

    public List<CountryEntity> getCountryEntitiesByCodes(List<String> countryCodes) {
        return countryService.getCountryEntitiesByCodes(countryCodes);
    }

    public List<CountryEntity> findByCodeIn(List<String> countryCodes) {
        return countryRepository.findByCodeIn(countryCodes);
    }
}

