package org.nikita.core.service.agreement;

import lombok.RequiredArgsConstructor;
import org.nikita.api.command.premium.TravelCalculatePremiumCoreCommand;
import org.nikita.api.dto.AgreementDto;
import org.nikita.core.domain.AgreementEntity;
import org.nikita.core.domain.CountryEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AgreementEntityFactory {

    private final AgreementCountryService agreementCountryService;

    public AgreementEntity createAgreementEntity(TravelCalculatePremiumCoreCommand dto) {
        AgreementEntity agreement = new AgreementEntity();

        List<CountryEntity> countries = agreementCountryService.findByCodeIn(dto.getAgreement().getCountriesToVisit());
        agreement.setCountriesToVisit(countries);

        agreement.setAgreementDateFrom(dto.getAgreement().getAgreementDateFrom() != null
                ? dto.getAgreement().getAgreementDateFrom()
                : LocalDateTime.now().plusDays(1));

        agreement.setAgreementDateTo(dto.getAgreement().getAgreementDateTo() != null
                ? dto.getAgreement().getAgreementDateTo()
                : LocalDateTime.now().plusDays(2));

        agreement.setInsuranceLimit(dto.getAgreement().getInsuranceLimit());
        agreement.setAgreementPremium(dto.getAgreement().getAgreementPremium());
        agreement.setSelectedRisks(dto.getAgreement().getSelectedRisks());
        agreement.setUuid(UUID.randomUUID());

        if (agreement.getPersons() == null) {
            agreement.setPersons(new ArrayList<>());
        }
        return agreement;
    }

    public AgreementEntity updateAgreementEntity(AgreementEntity existing, TravelCalculatePremiumCoreCommand dto) {
        AgreementDto dtoAgreement = dto.getAgreement();

        // Обновляем даты, если переданы
        if (dtoAgreement.getAgreementDateFrom() != null) {
            existing.setAgreementDateFrom(dtoAgreement.getAgreementDateFrom());
        }
        if (dtoAgreement.getAgreementDateTo() != null) {
            existing.setAgreementDateTo(dtoAgreement.getAgreementDateTo());
        }

        // Обновляем страны (загружаем сущности стран из кодов)
        List<CountryEntity> countries = agreementCountryService.findByCodeIn(dtoAgreement.getCountriesToVisit());
        existing.setCountriesToVisit(countries);

        // Обновляем лимит и премию
        existing.setInsuranceLimit(dtoAgreement.getInsuranceLimit());
        existing.setAgreementPremium(dtoAgreement.getAgreementPremium());

        // Обновляем риски
        existing.setSelectedRisks(dtoAgreement.getSelectedRisks());

        // Возможно обновление связей с пользователями:
        // Но обычно это делается отдельно в сервисе linkPersonsToAgreement
        // Поэтому здесь не делаем.

        return existing;
    }

}

