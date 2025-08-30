package org.nikita.core.underwriting.calcs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.RiskDto;
import org.nikita.core.service.ITravelPremiumCalculator;
import org.nikita.core.underwriting.ITravelPremiumUnderwriting;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
class TravelPremiumCalculator implements ITravelPremiumCalculator {

    private final ITravelPremiumUnderwriting premiumUnderwriting;

    @Override
    public void calculatePremiums(AgreementDto agreement) {
        calculateRiskPremiums(agreement);
        calculateTotalAgreementPremium(agreement);
    }

    private void calculateRiskPremiums(AgreementDto agreement) {
        log.debug("Calculating risk premiums for all persons");
        agreement.getPersons().forEach(person -> {
            log.debug("Calculating for person: {}", person.getFirstName());
            List<RiskDto> risks = premiumUnderwriting.calculateRiskPremiums(agreement, person);
            person.setRisks(risks);
        });
    }

    private void calculateTotalAgreementPremium(AgreementDto agreement) {
        BigDecimal totalPremium = agreement.getPersons().stream()
                .flatMap(person -> Optional.ofNullable(person.getRisks()).orElse(Collections.emptyList()).stream())
                .map(RiskDto::getPremium)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        agreement.setAgreementPremium(totalPremium);
        log.info("Total premium calculated: {}", totalPremium);
    }
}

