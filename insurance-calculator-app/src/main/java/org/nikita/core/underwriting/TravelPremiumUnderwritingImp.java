package org.nikita.core.underwriting;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.api.dto.RiskDto;
import org.nikita.core.util.IDateTimeService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TravelPremiumUnderwritingImp implements ITravelPremiumUnderwriting {

    private final IDateTimeService dateTimeService;
    private final SelectedRisksPremiumCalculator riskCalculator;
    @Override
    public BigDecimal calculateTripCost(AgreementDto request, PersonDto personDto) {
        if (request == null || request.getAgreementDateFrom() == null || request.getAgreementDateTo() == null) {
            return BigDecimal.ZERO;
        }
        var daysBetween = dateTimeService.getDaysBetween(request.getAgreementDateFrom(), request.getAgreementDateTo());

        BigDecimal dailyCost = BigDecimal.valueOf(10.00);

        return dailyCost.multiply(BigDecimal.valueOf(daysBetween));
    }

    @Override
    public BigDecimal calculateInsuranceCost(AgreementDto request, PersonDto personDto) {
        if (request == null || request.getAgreementDateFrom() == null || request.getAgreementDateTo() == null) {
            return BigDecimal.ZERO;
        }
        return calculateRiskPremiums(request, personDto).stream()
                .map(RiskDto::getPremium)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<RiskDto> calculateRiskPremiums(AgreementDto request, PersonDto personDto) {
        return riskCalculator.calculatePremiumForAllRisks(request, personDto);
    }

}
