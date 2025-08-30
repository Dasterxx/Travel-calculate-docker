package org.nikita.core.underwriting.calcs.evacuationAndRepatriation;

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
class TravelEvacuationAndRepatriationRiskPremiumCalculator implements TravelRiskPremiumCalculator {

    private final AgeCalcCoefficientEvacuationAndRepatriation ageCalcCoefficient;
    private final CountryCalcCoefficientEvacuationAndRepatriation countryCalcCoefficient;
    private final InsuranceLimitCalcCoefficientEvacuationAndRepatriation insuranceLimitCalcCoefficient;
    private final AgeCalculator ageCalculator;
    private final DayCountCalculatorEvacuationAndRepatriation dayCountCalculator;
    private final CountryDefaultDayRateEvacuationAndRepatriation countryDefaultDayRateCalculator;


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

        return baseCost.multiply(new BigDecimal("2.0"))
                .multiply(ageCoeff)
                .multiply(countryCoeff)
                .multiply(insuranceCoeff)
                .setScale(2, RoundingMode.HALF_UP);

    }

    @Override
    public String getRiskIc() {
        return "TRAVEL_EVACUATION";
    }
}

