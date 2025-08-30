package org.nikita.core.underwriting.calcs.thirdPartyLiability;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nikita.api.dto.AgreementDto;
import org.nikita.api.dto.PersonDto;
import org.nikita.core.underwriting.calcs.AgeCalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThirdPartyLiabilityRiskTest {

    @Mock
    private AgeCalcCoefficientThirdPartyLiability ageCalcCoefficient;

    @Mock
    private CountryCalcCoefficientThirdPartyLiability countryCalcCoefficient;

    @Mock
    private InsuranceLimitCalcCoefficientThirdPartyLiability insuranceLimitCalcCoefficient;

    @Mock
    private AgeCalculator ageCalculator;

    @Mock
    private DayCountCalculatorThirdPartyLiability dayCountCalculator;

    @Mock
    private CountryDefaultDayRateThirdPartyLiability countryDefaultDayRateCalculator;

    @InjectMocks
    private TravelThirdPartyLiabilityRiskPremiumCalculator calculator;

    private AgreementDto request;

    @BeforeEach
    void setup() {
        request = new AgreementDto();
        request.setAgreementDateFrom(LocalDateTime.of(2024, 1, 1, 0, 0));
        request.setAgreementDateTo(LocalDateTime.of(2024, 1, 3, 0, 0));

        PersonDto person = new PersonDto("John", "Doe", LocalDate.of(1994, 1, 1));
        request.setPersons(List.of(person));

        request.setCountriesToVisit(List.of("Spain"));
        request.setInsuranceLimit(BigDecimal.valueOf(10000));
    }

    @Test
    void testThirdPartyLiabilityRiskPremiumCalculator() {
        // Mock age and coefficients
        when(ageCalculator.calculateAge(any())).thenReturn(30);
        when(ageCalcCoefficient.getAgeCoefficient(anyInt())).thenReturn(BigDecimal.valueOf(1.3));
        when(countryCalcCoefficient.getCountryCoefficient(any())).thenReturn(BigDecimal.valueOf(1.1));
        when(insuranceLimitCalcCoefficient.getInsuranceLimitCoefficient(any())).thenReturn(BigDecimal.valueOf(1.0));

        // Mock day count and default day rate
        when(dayCountCalculator.calculateDayCount(request)).thenReturn(BigDecimal.valueOf(2)); // 2 days
        when(countryDefaultDayRateCalculator.calculateCountryDefaultDayRate(request)).thenReturn(BigDecimal.valueOf(10));

        // Calculate expected premium:
        // baseCost = days * countryDefaultDayRate = 2 * 10 = 20
        // premium = baseCost * 1.6 * ageCoeff * countryCoeff * insuranceCoeff
        BigDecimal baseCost = BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(10));
        BigDecimal expectedPremium = baseCost.multiply(new BigDecimal("1.6"))
                .multiply(BigDecimal.valueOf(1.3))
                .multiply(BigDecimal.valueOf(1.1))
                .multiply(BigDecimal.valueOf(1.0))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal premium = calculator.calculatePremium(request, request.getPersons().get(0));

        assertEquals(expectedPremium, premium);
    }
}
