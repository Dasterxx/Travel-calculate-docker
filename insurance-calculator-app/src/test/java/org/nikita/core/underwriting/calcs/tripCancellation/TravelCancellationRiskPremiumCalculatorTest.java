package org.nikita.core.underwriting.calcs.tripCancellation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.core.underwriting.calcs.AgeCalculator;
import org.nikita.core.underwriting.AvailableCountries;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TravelCancellationRiskPremiumCalculatorTest {

    @Mock
    private DayCountCalculatorCancellation dayCountCalculator;

    @Mock
    private CountryDefaultDayRateCancellation countryDefaultDayRateCalculator;

    @Mock
    private AgeCalcCoefficientCancellation ageCalcCoefficient;

    @Mock
    private CountryCancellationCalcCoefficient countryCalcCoefficient;

    @Mock
    private InsuranceLimitCalcCoefficientCancellation insuranceLimitCalcCoefficient;

    @Mock
    private AgeCalculator ageCalculator;

    @Mock
    private TravelCostCoefficient travelCostCoefficient;

    @Mock
    private CountrySafetyRatingCoefficient countrySafetyRatingCoefficient;

    @InjectMocks
    private TravelCancellationRiskPremiumCalculator calculator;

    private AgreementDto request;

    @BeforeEach
    void setup() {
        request = new AgreementDto();
        request.setAgreementDateFrom(LocalDateTime.of(2024, 1, 1, 0, 0));
        request.setAgreementDateTo(LocalDateTime.of(2024, 1, 3, 0, 0));

        PersonDto person1 = new PersonDto("John", "Doe", LocalDate.of(1994, 1, 1));
        PersonDto person2 = new PersonDto("Jane", "Doe", LocalDate.of(1980, 1, 1));
        PersonDto person3 = new PersonDto("Bob", "Smith", LocalDate.of(2000, 1, 1));

        request.setPersons(List.of(person1, person2, person3));
        request.setCountriesToVisit(List.of("SPAIN"));  // Испания с рейтингом 8
        request.setInsuranceLimit(BigDecimal.valueOf(10000));
    }

    @Test
    void testTravelCancellationRiskCalculatorPerPerson() {
        when(dayCountCalculator.calculateDayCount(any())).thenReturn(BigDecimal.valueOf(2));
        when(countryDefaultDayRateCalculator.calculateCountryDefaultDayRate(any())).thenReturn(BigDecimal.valueOf(50));
        when(countryCalcCoefficient.getCountryCoefficient(any())).thenReturn(BigDecimal.valueOf(1.1));
        when(insuranceLimitCalcCoefficient.getInsuranceLimitCoefficient(any())).thenReturn(BigDecimal.valueOf(1.0));
        when(travelCostCoefficient.calculateCoefficient(any())).thenReturn(BigDecimal.valueOf(1.0));

        when(ageCalculator.calculateAge(LocalDate.of(1994, 1, 1))).thenReturn(30);
        when(ageCalcCoefficient.calculateAgeCoefficient(30)).thenReturn(BigDecimal.valueOf(1.3));

        when(ageCalculator.calculateAge(LocalDate.of(1980, 1, 1))).thenReturn(43);
        when(ageCalcCoefficient.calculateAgeCoefficient(43)).thenReturn(BigDecimal.valueOf(1.5));

        when(ageCalculator.calculateAge(LocalDate.of(2000, 1, 1))).thenReturn(23);
        when(ageCalcCoefficient.calculateAgeCoefficient(23)).thenReturn(BigDecimal.valueOf(1.1));

        when(countrySafetyRatingCoefficient.calculate(
                eq(Collections.singletonList(AvailableCountries.SPAIN)),
                any(BigDecimal.class))
        ).thenReturn(new BigDecimal("0.125")); // 1/8




        BigDecimal baseCost = BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(50)); // 100

// Person 1 coefficients
        BigDecimal tripCostWithCoeffsPerson1 = baseCost.multiply(new BigDecimal("1.2"))
                .multiply(BigDecimal.valueOf(1.3))
                .multiply(BigDecimal.valueOf(1.1))
                .multiply(BigDecimal.valueOf(1.0))
                .multiply(BigDecimal.valueOf(1.0))
                .setScale(10, RoundingMode.HALF_UP);

// Safety coefficient = tripCostWithCoeffs / 8
        BigDecimal safetyCoefficientPerson1 = tripCostWithCoeffsPerson1.divide(BigDecimal.valueOf(8), 10, RoundingMode.HALF_UP);

// Final premium = tripCostWithCoeffs * safetyCoefficient = (tripCostWithCoeffs)^2 / 8
        BigDecimal expectedPremiumPerson1 = tripCostWithCoeffsPerson1.multiply(new BigDecimal("0.125"))
                .setScale(2, RoundingMode.HALF_UP);


        BigDecimal premiumPerson1 = calculator.calculatePremium(request, request.getPersons().get(0));
        assertEquals(expectedPremiumPerson1, premiumPerson1);


        // Person 2
        BigDecimal tripCostWithCoeffsPerson2 = baseCost.multiply(new BigDecimal("1.2"))
                .multiply(BigDecimal.valueOf(1.5))
                .multiply(BigDecimal.valueOf(1.1))
                .multiply(BigDecimal.valueOf(1.0))
                .multiply(BigDecimal.valueOf(1.0))
                .setScale(10, RoundingMode.HALF_UP);

        BigDecimal expectedPremiumPerson2 = tripCostWithCoeffsPerson2.multiply(BigDecimal.valueOf(0.125)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal premiumPerson2 = calculator.calculatePremium(request, request.getPersons().get(1));
        assertEquals(expectedPremiumPerson2, premiumPerson2);

        // Person 3
        BigDecimal tripCostWithCoeffsPerson3 = baseCost.multiply(new BigDecimal("1.2"))
                .multiply(BigDecimal.valueOf(1.1))
                .multiply(BigDecimal.valueOf(1.1))
                .multiply(BigDecimal.valueOf(1.0))
                .multiply(BigDecimal.valueOf(1.0))
                .setScale(10, RoundingMode.HALF_UP);

        BigDecimal expectedPremiumPerson3 = tripCostWithCoeffsPerson3.multiply(BigDecimal.valueOf(0.125)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal premiumPerson3 = calculator.calculatePremium(request, request.getPersons().get(2));
        assertEquals(expectedPremiumPerson3, premiumPerson3);
    }

}
