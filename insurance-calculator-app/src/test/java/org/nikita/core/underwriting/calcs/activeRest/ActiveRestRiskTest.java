package org.nikita.core.underwriting.calcs.activeRest;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActiveRestRiskTest {

    @Mock
    private AgeCalcCoefficientActiveRest ageCalcCoefficient;

    @Mock
    private CountryCalcCoefficientActiveRest countryCalcCoefficient;

    @Mock
    private InsuranceLimitCalcCoefficientActiveRest insuranceLimitCalcCoefficient;

    @Mock
    private AgeCalculator ageCalculator;

    @Mock
    private DayCountCalculatorActiveRest dayCountCalculator;

    @Mock
    private CountryDefaultDayRateActiveRest countryDefaultDayRateCalculator;

    @InjectMocks
    private TravelActiveRestRiskPremiumCalculator calculator;

    private AgreementDto request;
    private PersonDto person;

    @BeforeEach
    void setup() {
        request = new AgreementDto();
        request.setAgreementDateFrom(LocalDateTime.of(2024, 1, 1, 0, 0));
        request.setAgreementDateTo(LocalDateTime.of(2024, 1, 3, 0, 0));

        person = new PersonDto("John", "Doe", LocalDate.of(1994, 1, 1));
        request.setPersons(List.of(person));

        request.setCountriesToVisit(List.of("Spain"));
        request.setInsuranceLimit(BigDecimal.valueOf(10000));
    }

    @Test
    void testSportActivitiesRiskCalculator() {
        // Mock age calculation
        when(ageCalculator.calculateAge(any())).thenReturn(30);
        // Mock coefficients
        when(ageCalcCoefficient.getAgeCoefficient(anyInt())).thenReturn(BigDecimal.valueOf(1.3));
        when(countryCalcCoefficient.getCountryCoefficient(any())).thenReturn(BigDecimal.valueOf(1.1));
        when(insuranceLimitCalcCoefficient.getInsuranceLimitCoefficient(any())).thenReturn(BigDecimal.valueOf(1.0));
        // Mock day count and country default day rate
        when(dayCountCalculator.calculateDayCount(request)).thenReturn(BigDecimal.valueOf(2)); // 2 days
        when(countryDefaultDayRateCalculator.calculateCountryDefaultDayRate(request)).thenReturn(BigDecimal.valueOf(10)); // 10 per day

        // Base cost = days * country default day rate = 2 * 10 = 20
        BigDecimal baseCost = BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(10));

        // Fixed multiplier in production code is 1.5
        BigDecimal expectedPremium = baseCost.multiply(new BigDecimal("1.2"))
                .multiply(BigDecimal.valueOf(1.3))
                .multiply(BigDecimal.valueOf(1.1))
                .multiply(BigDecimal.valueOf(1.0))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal premium = calculator.calculatePremium(request, person);

        assertEquals(expectedPremium, premium);
    }
}
