package org.nikita.core.underwriting;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.RiskDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
class SelectedRisksPremiumCalculator {
    private final List<TravelRiskPremiumCalculator> riskCalculators;

    public List<RiskDto> calculatePremiumForAllRisks(AgreementDto request, PersonDto personDto) {
        List<RiskDto> riskDtos = new ArrayList<>();
        if (request == null || request.getSelectedRisks() == null) {
            log.warn("Invalid request received: {}", request);
            return Collections.emptyList();
        }
        for (String riskIc : request.getSelectedRisks()) {
            Optional<RiskDto> riskDto = calculateRiskPremium(request, riskIc, personDto);
            riskDto.ifPresent(riskDtos::add);
        }
        log.info("Calculated premiums for selected risks: {}", riskDtos);
        return riskDtos;
    }

    private Optional<RiskDto> calculateRiskPremium(AgreementDto request, String riskIc, PersonDto personDto) {
        Optional<TravelRiskPremiumCalculator> calculator = riskCalculators.stream()
                .filter(c -> c.getRiskIc().equals(riskIc))
                .findFirst();

        if (calculator.isEmpty()) {
            log.warn("No calculator found for risk: {}", riskIc);
            return Optional.empty();
        }

        BigDecimal premium = calculator.get().calculatePremium(request, personDto);
        return Optional.of(new RiskDto(riskIc, premium));
    }
}
