package org.nikita.core.underwriting.calcs.tripCancellation;

import lombok.RequiredArgsConstructor;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.core.underwriting.AvailableCountries;
import org.nikita.core.underwriting.TravelRiskPremiumCalculator;
import org.nikita.core.underwriting.calcs.AgeCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class TravelCancellationRiskPremiumCalculator implements TravelRiskPremiumCalculator {

    private final AgeCalcCoefficientCancellation ageCalcCoefficient;
    private final CountryCancellationCalcCoefficient countryCalcCoefficient;
    private final InsuranceLimitCalcCoefficientCancellation insuranceLimitCalcCoefficient;
    private final CountryDefaultDayRateCancellation countryDefaultDayRateCalculator;
    private final DayCountCalculatorCancellation dayCountCalculator;
    private final AgeCalculator ageCalculator;
    private final TravelCostCoefficient travelCostCoefficient;
    private final CountrySafetyRatingCoefficient countrySafetyRatingCoefficient;

    @Override
    public BigDecimal calculatePremium(AgreementDto request, PersonDto person) {
        BigDecimal daysBetween = dayCountCalculator.calculateDayCount(request);
        BigDecimal baseCost = daysBetween.multiply(
                countryDefaultDayRateCalculator.calculateCountryDefaultDayRate(request)
        );

        int age = ageCalculator.calculateAge(person.getBirthDate());

        BigDecimal ageCoeff =
                Optional.ofNullable(ageCalcCoefficient.calculateAgeCoefficient(age)).orElse(BigDecimal.ONE);
        BigDecimal countryCoeff =
                Optional.ofNullable(countryCalcCoefficient.getCountryCoefficient(request.getCountriesToVisit())).orElse(BigDecimal.ONE);
        BigDecimal insuranceCoeff =
                Optional.ofNullable(insuranceLimitCalcCoefficient.getInsuranceLimitCoefficient(request.getInsuranceLimit())).orElse(BigDecimal.ONE);
        BigDecimal travelCostCoeff =
                Optional.ofNullable(travelCostCoefficient.calculateCoefficient(request.getInsuranceLimit())).orElse(BigDecimal.ONE);

        // Расчёт стоимости с учётом коэффициентов
        BigDecimal tripCostWithCoefficients = baseCost.multiply(new BigDecimal("1.2"))
                .multiply(ageCoeff)
                .multiply(countryCoeff)
                .multiply(insuranceCoeff)
                .multiply(travelCostCoeff)
                .setScale(2, RoundingMode.HALF_UP);

        // Получаем страну из списка (берём первую, если несколько)
        List<AvailableCountries> countries = request.getCountriesToVisit().stream()
                .map(AvailableCountries::valueOf)
                .toList();

        // Рассчитываем коэффициент безопасности страны
        BigDecimal safetyCoefficient = Optional.ofNullable(
                countrySafetyRatingCoefficient.calculate(countries, tripCostWithCoefficients)
        ).orElse(BigDecimal.ONE);
//        BigDecimal safetyCoefficient = Optional.ofNullable(countrySafetyRatingCoefficient.calculate(countries, tripCostWithCoefficients))
//                .orElse(BigDecimal.ONE);

        BigDecimal finalPremium = tripCostWithCoefficients.multiply(safetyCoefficient)
                .setScale(2, RoundingMode.HALF_UP);

        return finalPremium;
    }


    @Override
    public String getRiskIc() {
        return "TRAVEL_CANCELLATION";
    }
}

