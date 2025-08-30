package org.nikita.core.underwriting.calcs.service;

import lombok.RequiredArgsConstructor;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.RiskDto;
import org.nikita.core.underwriting.ITravelPremiumUnderwriting;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Component
public class RiskPremiumCalculator implements IRiskPremiumCalculator {

    private final ITravelPremiumUnderwriting underwriting;

    @Override
    public BigDecimal calculateRiskPremium(AgreementDto agreement, PersonDto person, String riskIc) {
        List<RiskDto> risks = underwriting.calculateRiskPremiums(agreement, person);
        return risks.stream()
                .filter(r -> r.getRiskIc().equals(riskIc))
                .map(RiskDto::getPremium)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }
}

