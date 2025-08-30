package org.nikita.core.underwriting.calcs.thirdPartyLiability;

import lombok.RequiredArgsConstructor;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.core.underwriting.TravelRiskPremiumCalculator;
import org.nikita.core.underwriting.calcs.AgeCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class TravelThirdPartyLiabilityRiskPremiumCalculator implements TravelRiskPremiumCalculator {

    private final AgeCalcCoefficientThirdPartyLiability ageCalcCoefficient;
    private final CountryCalcCoefficientThirdPartyLiability countryCalcCoefficient;
    private final InsuranceLimitCalcCoefficientThirdPartyLiability insuranceLimitCalcCoefficient;
    private final AgeCalculator ageCalculator;
    private final DayCountCalculatorThirdPartyLiability dayCountCalculator;
    private final CountryDefaultDayRateThirdPartyLiability countryDefaultDayRateCalculator;

    @Override
    public BigDecimal calculatePremium(AgreementDto request, PersonDto personDto) {
        BigDecimal daysBetween = dayCountCalculator.calculateDayCount(request);
        BigDecimal baseCost = daysBetween.multiply(
                countryDefaultDayRateCalculator.calculateCountryDefaultDayRate(request)
        );

        int age = ageCalculator.calculateAge(personDto.getBirthDate());

        BigDecimal ageCoeff =
                Optional.ofNullable(ageCalcCoefficient.getAgeCoefficient(age)).orElse(BigDecimal.ONE);
        BigDecimal countryCoeff =
                Optional.ofNullable(countryCalcCoefficient.getCountryCoefficient(request.getCountriesToVisit())).orElse(BigDecimal.ONE);
        BigDecimal insuranceCoeff =
                Optional.ofNullable(insuranceLimitCalcCoefficient.getInsuranceLimitCoefficient(request.getInsuranceLimit())).orElse(BigDecimal.ONE);

        return baseCost.multiply(new BigDecimal("1.6"))
                .multiply(ageCoeff)
                .multiply(countryCoeff)
                .multiply(insuranceCoeff)
                .setScale(2, RoundingMode.HALF_UP);

    }

    @Override
    public String getRiskIc() {
        return "TRAVEL_THIRD_PARTY_LIABILITY";
    }
}

