package org.nikita.core.domain.mapper;

import lombok.RequiredArgsConstructor;
import org.nikita.api.dto.AgreementDto;
import org.nikita.core.domain.AgreementEntity;
import org.nikita.core.domain.CountryEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AgreementMapper {

    private final PersonMapper personMapper;

    public AgreementDto toDto(AgreementEntity entity) {
        List<String> countryCodes = entity.getCountriesToVisit().stream()
                .map(CountryEntity::getCode)
                .toList();

        return AgreementDto.builder()
                .agreementDateFrom(entity.getAgreementDateFrom())
                .agreementDateTo(entity.getAgreementDateTo())
                .countriesToVisit(countryCodes)
                .insuranceLimit(entity.getInsuranceLimit())
                .agreementPremium(entity.getAgreementPremium())
                .selectedRisks(entity.getSelectedRisks())
                .persons(entity.getPersons().stream()
                        .map(personMapper::toResponseDto)
                        .toList())
                .uuid(entity.getUuid())
                .build();
    }



}

